(ns minimily.inventory.model.location
  (:require [minimily.utils.database :as db]))

(def table :location)

 (defn find-all []
  (db/find-records "select * from location order by name"))

(defn get-it [id]
  (db/get-record table id))

(defn save [location]
  (db/save-record table location))

(defn delete-it [id]
  (db/delete-record table id))