(ns minimily.documents.model.document
  (:require [hugsql.core                     :as hugsql]
            [minimily.utils.database         :as db]
            [minimily.documents.model.folder :as folder-model]
            [amazonica.aws.s3                :as s3]
            [config.core                     :refer [env]]))

; Datastore definitions
(hugsql/def-sqlvec-fns "minimily/documents/model/sql/document.sql")
(def table  :document)

; Filestore definitions
(def bucket (or (:AWS_S3_BUCKET_NAME env) (System/getenv "AWS_S3_BUCKET_NAME")))
(def cred {:access-key (or (:AWS_ACCESS_KEY_ID env) (System/getenv "AWS_ACCESS_KEY_ID"))
           :secret-key (or (:AWS_SECRET_ACCESS_KEY env) (System/getenv "AWS_SECRET_ACCESS_KEY"))
           :endpoint   (or (:AWS_ENDPOINT env) (System/getenv "AWS_ENDPOINT"))})

(defn find-by-folder [folder-id]
  (db/find-records (documents-by-folder-sqlvec {:folder-id folder-id})))

(defn count-documents [folder-id]
  (:count (first (db/find-records (documents-count-in-folder-sqlvec {:folder-id folder-id})))))

(defn count-siblings [profile-id folder-id]
  (let [num-folders (folder-model/count-children profile-id folder-id)
        num-documents (:count (first (db/find-records 
                                (documents-count-in-folder-sqlvec {:folder-id folder-id}))))]
    (+ num-folders num-documents)))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn get-file [profile-id id]
  (let [document (get-it profile-id id)]
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

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))

(defn find-path [profile-id id]
  (let [document    (get-it profile-id id)
        document    (conj document {:name (:title document)})
        folder-path (folder-model/find-path profile-id (:folder document))]
    (concat folder-path [document])))