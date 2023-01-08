(ns minimily.inventory.model.collection
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]))

(hugsql/def-sqlvec-fns "minimily/inventory/model/sql/collection.sql")

(def table :collection)

(defn find-all [profile-id]
  (db/find-records 
    (collections-by-profile-sqlvec {:profile-id profile-id})))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [collection]
  (db/save-record table collection))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))