(ns minimily.inventory.model.collection
  (:require [minimily.utils.database :as db]))

(def table :collection)

(defn find-all [profile-id]
  (db/find-records ["select * from collection where profile = ? order by name" profile-id]))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [collection]
  (db/save-record table collection))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))