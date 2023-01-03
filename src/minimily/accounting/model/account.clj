(ns minimily.accounting.model.account
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model])
  (:import (java.math RoundingMode)))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/account.sql")

(def table :account)

(defn find-actives [profile]
  (let [family-members (family-member-model/list-family-organizers profile)]
    (db/find-records (active-accounts-sqlvec {:ids family-members}))))

(defn find-inactives [profile]
  (let [family-members (family-member-model/list-family-organizers profile)]
    (db/find-records (inactive-accounts-sqlvec {:ids family-members}))))

(defn find-actives-except [profile except-id]
  (filter #(not= (:id %) except-id) 
          (find-actives profile)))

(defn find-third-party-accounts [third-party-id]
  (let [third-party-id (try (Integer/parseInt third-party-id) (catch Exception e 0))]
    (db/find-records (third-party-accounts-sqlvec {:third-party-id third-party-id}))))

(defn get-it [profile id]
  (db/get-record table id))

(defn save [account]
  (db/save-record table account))

(defn update-balance [id new-balance]
  (db/update-record table {:id id :balance new-balance})
  new-balance)

(defn percentage-used-credit [account]
  (let [balance     (if (nil? (:balance account))     0 (:balance account))
        debit-limit (if (nil? (:debit_limit account)) 0 (:debit_limit account))]
    (if (or (<= balance 0)
            (<= debit-limit 0))
      0
      (Math/abs (.intValue (.divide (.multiply balance (BigDecimal. 100)) 
                                    debit-limit
                                    RoundingMode/HALF_UP))))))

(defn delete-it [profile id]
  (db/delete-record table id profile))