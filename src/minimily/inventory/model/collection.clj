(ns minimily.inventory.model.collection
  (:require [minimily.utils.database :as db]))

(def table :collection)

 (defn find-all []
  (db/find-records "select * from collection order by name"))

(defn get-it [id]
  (db/get-record table id))

(defn save [collection]
  (db/save-record table collection))

(defn delete-it [id]
  (db/delete-record table id))