(ns minimily.accounting.model.transaction
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/transaction.sql")

(def table :transaction)

(defn find-by-account [profile-id account-id]
  (let [family-members (family-member-model/list-family-organizers profile-id)]
    (db/find-records (transactions-by-profile-account-sqlvec {:profile-ids family-members 
                                                              :account-id (Integer/parseInt account-id)}))))

(defn calculate-balance [account-id]
  (let [transactions (transactions-by-account-sqlvec {:account-id account-id})
        amounts      (map #(* (:type %) (:amount %)) transactions)]
    (reduce + amounts)))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [transaction]
  (db/save-record table transaction))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))