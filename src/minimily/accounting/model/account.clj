(ns minimily.accounting.model.account
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/account.sql")

(def table :account)

(defn find-actives [holder]
  (let [family-members (family-member-model/list-family-organizers holder)]
    (db/find-records (active-accounts-sqlvec {:ids family-members}))))

(defn find-inactives [holder]
  (let [family-members (family-member-model/list-family-organizers holder)]
    (db/find-records (inactive-accounts-sqlvec {:ids family-members}))))

(defn find-actives-except [holder except-id]
  (filter #(not= (:id %) except-id) 
          (find-actives holder)))

(defn get-it [holder id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn update-balance [id new-balance]
  (db/update-record table {:id id :balance new-balance})
  new-balance)

(defn delete-it [holder id]
  (db/delete-record table id holder))