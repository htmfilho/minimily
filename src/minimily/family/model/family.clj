(ns minimily.family.model.family
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]))

(hugsql/def-sqlvec-fns "minimily/family/model/sql/family.sql")

(def table :account)

(defn get-it [holder id]
  (db/get-record table id))

(defn find-family-organizer [profile-id]
  (first (db/find-records (family-of-organizer-sqlvec {:profile-id profile-id}))))

(defn save [family]
  (db/save-record table family))

(defn delete-it [holder id]
  (db/delete-record table id holder))