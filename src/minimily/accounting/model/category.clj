(ns minimily.accounting.model.category
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.utils.string               :as s]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/category.sql")

(def table :transaction_category)

(defn find-parents [profile-id]
  (db/find-records 
    (categories-sqlvec 
      {:profile-ids 
          (family-member-model/list-family-organizers profile-id)})))

(defn find-children [profile-id parent-id]
  (db/find-records 
    (categories-children-sqlvec 
      {:profile-ids (family-member-model/list-family-organizers profile-id)
       :parent-id   parent-id})))

(defn- find-categories [profile-id query-func]
  (db/find-records
    (query-func {:profile-ids (family-member-model/list-family-organizers profile-id)})))

(defn find-debit-categories [profile-id]
  (find-categories profile-id debit-categories-sqlvec))

(defn find-credit-categories [profile-id]
  (find-categories profile-id credit-categories-sqlvec))

(defn count-children [profile-id parent-id]
  (:count (first 
            (db/find-records 
              (categories-count-children-sqlvec 
                {:profile-ids (family-member-model/list-family-organizers profile-id)
                 :parent-id   parent-id})))))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [category]
  (db/save-record table category))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))

(defn find-path [profile-id id]
  (reverse (loop [category (get-it profile-id id)
                  path []]
             (if (nil? (:parent category))
               (conj path category)
               (let [parent (get-it profile-id (:parent category))]
                 (recur parent
                        (conj path category)))))))

(defn path [profile-id id]
  (reduce #(str %1 "/" %2) (find-path profile-id id)))