(ns minimily.documents.model.document
  (:require [minimily.utils.database         :as db]
            [minimily.documents.model.folder :as folder-model]
            [amazonica.aws.s3                :as s3]
            [config.core                     :refer [env]]))

(def table  :document)
(def bucket (:AWS_S3_BUCKET_NAME env))
(def cred {:access-key (:AWS_ACCESS_KEY_ID env)
           :secret-key (:AWS_SECRET_ACCESS_KEY env)
           :endpoint   (:AWS_ENDPOINT env)})

(defn find-by-folder [folder-id]
  (db/find-records (str "select * from document where folder = " folder-id)))

(defn count-documents [folder-id]
  (:count (first (db/find-records (str "select count(*) from document where folder = " folder-id)))))

(defn count-siblings [folder-id]
  (let [num-folders (folder-model/count-children folder-id)
        num-documents (:count (first (db/find-records (str "select count(*) from document where folder = " folder-id))))]
    (+ num-folders num-documents)))

(defn get-it [id]
  (db/get-record table id))

(defn get-file [id]
  (let [document (get-it id)]
    (s3/get-object cred 
                   :bucket-name bucket
                   :key (:file_store_path document))))

(defn save-file [file document]
  (s3/put-object cred
                 :bucket-name bucket
                 :key (:file_store_path document)
                 :file file)
  document)

(defn save [document file]
  (let [id (db/save-record table document)]
    (save-file file document)
    id))

(defn delete-it [id]
  (db/delete-record table id))

(defn find-path [id]
  (let [document    (get-it id)
        document    (conj document {:name (:title document)})
        folder-path (folder-model/find-path (:folder document))]
    (concat folder-path [document])))