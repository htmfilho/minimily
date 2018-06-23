(ns minimily.accounting.model.currency
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/currency.sql")

(def table :currency)

(defn find-all [& [except]]
  (let [currencies (db/find-records (currencies-sqlvec))]
    (if except
      (filter #(not= (:id %) except) currencies)
      currencies)))

(defn get-it [id]
  (db/get-record table id))