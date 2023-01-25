(ns minimily.accounting.model.transfer
  (:require [hugsql.core                           :as hugsql]
            [minimily.utils.database               :as db]
            [minimily.accounting.model.account     :as account-model]
            [minimily.accounting.model.transaction :as transaction-model]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/transfer.sql")

(def table :account_transfer)

(defn get-it [id]
  (db/get-record table id))

(defn save [transfer]
  (println transfer)
  (db/save-record table transfer))

(defn transfer [transaction-from transaction-to]
  (transaction-model/save transaction-from)
  (account-model/update-balance (:account transaction-from)
                                (+ (* (:type transaction-from) 
                                      (:amount transaction-from))
                                   (transaction-model/calculate-balance (:account transaction-from))))
    
  (transaction-model/save transaction-to)
  (account-model/update-balance (:account transaction-to)
                                (+ (* (:type transaction-to)
                                      (:amount transaction-to))
                                   (transaction-model/calculate-balance (:account transaction-to)))))

(defn find-transfers-from-profile [profile-id]
  (db/find-records (transfers-from-profile-sqlvec {:profile-id profile-id})))

(defn find-transfers-to-profile [profile-id]
  (db/find-records (transfers-to-profile-sqlvec {:profile-id profile-id})))