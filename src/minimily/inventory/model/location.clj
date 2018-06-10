(ns minimily.inventory.model.location
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/inventory/model/sql/location.sql")

(def table :location)

(defn find-all [profile-id]
  (db/find-records 
    (locations-by-profile-sqlvec 
      {:profile-ids (family-member-model/list-family-organizers profile-id)})))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [location]
  (db/save-record table location))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))