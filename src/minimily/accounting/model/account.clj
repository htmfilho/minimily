(ns minimily.accounting.model.account
  (:require [minimily.utils.database :as db]
            [minimily.core.model.family-member :as family-member-model]))

(def table :account)

(defn find-all [holder]
  (let [family-members (map #(:user_profile %) 
                       (family-member-model/find-members-same-family holder))]
    (if (empty? family-members)
      (db/find-records (str "select * from account where holder = " holder))
      (db/find-records (str "select * from account where holder in " 
                            (str "(" (reduce #(str %1 "," %2) family-members) ")"))))))

(defn find-all-except [holder except-id]
  (filter #(not= (:holder %) except-id) 
          (find-all holder)))

(defn get-it [holder id]
  (db/get-record table id holder))

(defn save [account]
  (db/save-record table account))

(defn update-balance [id new-balance]
  (db/update-record table {:id id :balance new-balance})
  new-balance)

(defn delete-it [holder id]
  (db/delete-record table id holder))