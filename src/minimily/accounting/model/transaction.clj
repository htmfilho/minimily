(ns minimily.accounting.model.transaction
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model]
            [minimily.accounting.model.account   :as account-model]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/transaction.sql")

(def table :transaction)
(def DEBIT -1)
(def CREDIT 1)

(defn find-by-account [profile-id account-id]
  (let [family-members (family-member-model/list-family-organizers profile-id)]
    (db/find-records (transactions-by-profile-account-sqlvec {:profile-ids family-members 
                                                              :account-id (Integer/parseInt account-id)}))))

(defn find-balance-history [account-id]
  (db/find-records 
    (transactions-balance-history-sqlvec {:account-id (Integer/parseInt account-id)})))

(defn calculate-balance [account-id]
  (let [transactions (db/find-records (transactions-by-account-sqlvec {:account-id account-id}))
        amounts      (map #(* (:type %) (:amount %)) transactions)]
    (reduce + amounts)))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [transaction]
  (db/save-record table transaction))

(defn add [transaction]
  (account-model/update-balance (:account transaction)
                                (+ (* (:type transaction) 
                                      (:amount transaction))
                                   (calculate-balance (:account transaction))))
  (save transaction))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))