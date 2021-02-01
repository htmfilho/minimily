(ns minimily.accounting.model.third-party
  (:require [hugsql.core             :as hugsql]
            [minimily.utils.database :as db]))

(hugsql/def-sqlvec-fns "minimily/accounting/model/sql/third_party.sql")

(def table :third_party)

(defn save [third-party]
  (db/save-record table third-party))

(defn get-it [id]
  (db/get-record table id))

(defn find-all [& [except]]
  (let [third-parties (db/find-records (third-parties-sqlvec))]
    (if except
      (filter #(not= (:id %) except) third-parties)
      third-parties)))

(defn delete-it [id]
  (db/delete-record table id))