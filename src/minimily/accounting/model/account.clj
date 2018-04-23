(ns minimily.accounting.model.account
  (:require [minimily.utils.database :as db]
            [minimily.family.model.family-member :as family-member-model]))

(def table :account)

(defn find-all [holder]
  (let [family-members (family-member-model/list-family-organizers holder)]
    (db/find-records (str "select * from account where holder in (" family-members ")"))))

(defn find-all-except [holder except-id]
  (filter #(not= (:id %) except-id) 
          (find-all holder)))

(defn get-it [holder id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn update-balance [id new-balance]
  (db/update-record table {:id id :balance new-balance})
  new-balance)

(defn delete-it [holder id]
  (db/delete-record table id holder))