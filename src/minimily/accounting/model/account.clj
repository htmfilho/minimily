(ns minimily.accounting.model.account
  (:require [minimily.utils.database :as db]
            [minimily.core.model.family-member :as family-member-model]))

(def table :account)

(defn find-all [profile-id]
  (let [family-members (map #(:user_profile %) 
                       (family-member-model/find-members-same-family profile-id))]
    (if (empty? family-members)
      (db/find-records (str "select * from account where holder = " profile-id))
      (db/find-records (str "select * from account where holder in " (str "(" (reduce #(str %1 "," %2) family-members) ")"))))))

(defn find-all-except [profile-id except-id]
  (filter #(not= (:holder %) except-id) 
          (find-all profile-id)))

(defn get-it [id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn update-balance [id new-balance]
  (db/update-record table {:id id :balance new-balance})
  new-balance)

(defn delete-it [id]
  (db/delete-record table id))