(ns minimily.inventory.model.good
  (:require [minimily.utils.database :as db]))

(defn find-by-location [location-id]
  (db/find-records (str "select * from good where location = " location-id)))