(ns minimily.accounting.model.account
  (:require [minimily.utils.database :as db]))

(def table :account)

(defn find-all [profile-id]
  (db/find-records (str "select * from account where holder = " profile-id)))

(defn find-all-except [profile-id except-id]
  (db/find-records (str "select * from account where holder = " profile-id " and id <> " except-id)))

(defn get-it [id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn update-balance [id new-balance]
  (db/update-record table {:id id :balance new-balance})
  new-balance)

(defn delete-it [id]
  (db/delete-record table id))