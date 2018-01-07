(ns minimily.accounting.model.account
  (:require [minimily.utils.database :as db]))

(def table :account)

(defn find-all [profile-id]
  (db/find-records (str "select * from account where holder = " 
                        (if profile-id profile-id 0))))

(defn get-it [id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn delete-it [id]
  (db/delete-record table id))