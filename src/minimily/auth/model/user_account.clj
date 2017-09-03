(ns minimily.auth.model.user-account
  (:require [minimily.utils.database :as db]))

(def table :user_account)

(defn get [id]
  (db/get-record table id))

(defn save [record]
  (db/save-record table record))

