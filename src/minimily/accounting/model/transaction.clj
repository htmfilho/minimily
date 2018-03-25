(ns minimily.accounting.model.transaction
  (:require [minimily.utils.database :as db]))

(def table :transaction)

(defn find-by-account [account-id]
  (db/find-records (str "select * from transaction where account = " 
                        account-id)))

(defn get-it [id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn delete-it [id]
  (db/delete-record table id))