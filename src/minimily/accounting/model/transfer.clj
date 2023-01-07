(ns minimily.accounting.model.transfer
  (:require [hugsql.core             :as hugsql]
            [minimily.utils.database :as db]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/transfer.sql")

(def table :account_transfer)

(defn get-it [id]
  (db/get-record table id))

(defn save [transfer]
  (println transfer)
  (db/save-record table transfer))

(defn find-transfers-from-profile [profile-id]
  (db/find-records (transfers-from-profile-sqlvec {:profile-id profile-id})))

(defn find-transfers-to-profile [profile-id]
  (db/find-records (transfers-to-profile-sqlvec {:profile-id profile-id})))