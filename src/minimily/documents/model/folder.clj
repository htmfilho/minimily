(ns minimily.documents.model.folder
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.utils.string               :as s]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/documents/model/sql/folder.sql")

(def table :folder)

(defn find-parents [profile-id]
  (db/find-records 
    (folders-by-profile-sqlvec 
      {:profile-ids 
          (family-member-model/list-family-organizers profile-id)})))

(defn find-children [profile-id parent-id]
  (db/find-records (folders-children-sqlvec {:profile-ids (family-member-model/list-family-organizers profile-id)
                                             :parent-id   parent-id})))

(defn count-children [profile-id parent-id]
  (:count (first (db/find-records (folders-count-children-sqlvec 
                                     {:profile-ids (family-member-model/list-family-organizers profile-id)
                                      :parent-id   parent-id})))))

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
                (let [parent (get-it profile-id (:parent folder))]
                  (recur parent 
                        (conj path folder)))))))

(defn path [profile-id id]
  (reduce #(str %1 "/" %2) (map #(s/tech (:name %)) (find-path profile-id id))))