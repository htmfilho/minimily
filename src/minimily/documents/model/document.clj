(ns minimily.documents.model.document
  (:require [minimily.utils.database         :as db]
            [minimily.documents.model.folder :as folder-model]))

(def table :document)

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

(defn save [folder]
  (db/save-record table folder))

(defn delete-it [id]
  (db/delete-record table id))

(defn find-path [id]
  (let [document (get-it id)]
    (conj (folder-model/find-path (:folder document)) document)))