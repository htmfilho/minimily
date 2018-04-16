(ns minimily.inventory.model.location
  (:require [minimily.utils.database :as db]))

(def table :location)

 (defn find-all [profile-id]
  (db/find-records ["select * from location where profile = ? order by name" profile-id]))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [location]
  (db/save-record table location))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))