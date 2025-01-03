(ns minimily.accounting.model.transaction
  (:require [hugsql.core             :as hugsql]
            [clojure.java.jdbc       :as jdbc]
            [minimily.utils.database :as db]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/transaction.sql")

(def table :transaction)
(def DEBIT -1)
(def CREDIT 1)

(defn find-by-account [profile-id account-id]
  (db/find-records 
    (transactions-by-profile-account-sqlvec 
      {:profile-id profile-id 
       :account-id (Integer/parseInt account-id)})))

(defn find-balance-history [account-id]
  (db/find-records 
    (transactions-balance-history-sqlvec 
      {:account-id (Integer/parseInt account-id)})))

(defn find-by-category [category-id]
  (db/find-records 
    (transactions-category-sqlvec {:category-id category-id})))

(defn calculate-balance [account-id]
  (let [transactions (db/find-records 
                       (transactions-by-account-sqlvec 
                         {:account-id account-id}))
        amounts      (map #(* (:type %) (:amount %)) transactions)]
    (reduce + amounts)))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [transaction]
  (db/save-record table transaction))

(defn create-counter-transaction [transaction third-party-account]
  (-> transaction 
      (conj {:type (* (:type transaction) DEBIT)})
      (conj {:account_transfer (:account transaction)})
      (conj {:account third-party-account})))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))

(defn delete-all-account [account-id profile-id]
  (db/with-conn
    (jdbc/delete! conn table ["account = ? and profile = ?" account-id profile-id])))