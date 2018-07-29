(ns minimily.accounting.model.account
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model])
  (:import (java.math RoundingMode)))

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

(defn percentage-used-credit [account]
  (let [balance     (:balance account)
        debit-limit (:debit_limit account)]
    (if (or (>= (.intValue balance) 0) (nil? debit-limit))
      0
      (Math/abs (.intValue (.divide (.multiply balance (BigDecimal. 100)) 
                                    debit-limit
                                    RoundingMode/HALF_UP))))))

(defn delete-it [holder id]
  (db/delete-record table id holder))