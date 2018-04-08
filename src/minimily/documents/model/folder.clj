(ns minimily.documents.model.folder
  (:require [minimily.utils.database :as db]
            [minimily.utils.string   :as s]))

(def table :folder)

(defn find-parents []
  (db/find-records (str "select * from folder where parent is null")))

(defn find-children [parent-id]
  (db/find-records (str "select * from folder where parent = " parent-id)))

(defn count-children [parent-id]
  (:count (first (db/find-records (str "select count(*) from folder where parent = " parent-id)))))

(defn get-it [id]
  (db/get-record table id))

(defn save [folder]
  (db/save-record table folder))

(defn delete-it [id]
  (db/delete-record table id))

(defn find-path [id]
  (reverse (loop [folder (get-it id)
                  path []]
              (if (nil? (:parent folder))
                (conj path folder)
                (let [parent (get-it (:parent folder))]
                  (recur parent 
                        (conj path folder)))))))

(defn path [id]
  (reduce #(str %1 "/" %2) (map #(s/tech (:name %)) (find-path id))))