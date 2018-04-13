(ns minimily.documents.model.folder
  (:require [minimily.utils.database :as db]
            [minimily.utils.string   :as s]))

(def table :folder)

(defn find-parents [profile-id]
  (db/find-records (str "select * from folder where profile = " profile-id " parent is null")))

(defn find-children [profile-id parent-id]
  (db/find-records (str "select * from folder where profile = " profile-id " parent = " parent-id)))

(defn count-children [profile-id parent-id]
  (:count (first (db/find-records (str "select count(*) from folder where profile = " profile-id " parent = " parent-id)))))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [folder]
  (db/save-record table folder))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))

(defn find-path [profile-id id]
  (reverse (loop [folder (get-it profile-id id)
                  path []]
              (if (nil? (:parent folder))
                (conj path folder)
                (let [parent (get-it (:parent folder))]
                  (recur parent 
                        (conj path folder)))))))

(defn path [profile-id id]
  (reduce #(str %1 "/" %2) (map #(s/tech (:name %)) (find-path profile-id id))))