(ns minimily.inventory.model.good
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/inventory/model/sql/good.sql")

(def table :good)

(defn find-by-location [profile-id location-id]
  (db/find-records 
    (goods-by-location-sqlvec 
      {:profile-ids (family-member-model/list-family-organizers profile-id)
       :location-id location-id})))

(defn find-by-collection [profile-id collection-id]
  (db/find-records 
    (goods-by-collection-sqlvec 
      {:profile-ids (family-member-model/list-family-organizers profile-id)
       :collection-id collection-id})))

(defn find-by-criteria [profile-id location-id collection-id]
  (let [location?   (not (empty? location-id))
        collection? (not (empty? collection-id))]
    (if (or location? collection?)
      (let [query (str "select * from good where profile = " profile-id " and "
                      (when location? (str "location = " location-id))
                      (when (and location? collection?) " and ")
                      (when collection? (str "collection = " collection-id)))]
        (db/find-records query))
      [])))

(defn get-it [profile-id id]
  (let [goods (db/find-records
                (good-sqlvec 
                  {:id id
                   :profile-ids (family-member-model/list-family-organizers profile-id)}))]
    (if (empty? goods)
      nil
      (first goods))))

(defn save [good]
  (db/save-record table good))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))