(ns minimily.accounting.model.transaction
  (:require [minimily.utils.database :as db]))

(def table :transaction)

(defn find-by-account [profile-id account-id]
  (db/find-records (str "select * from transaction where profile = " profile-id " and account = " account-id " order by date_transaction desc")))

(defn calculate-balance [profile-id account-id]
  (let [transactions (db/find-records (str "select type, amount from transaction where profile = " profile-id " and account = " account-id))
        amounts      (map #(* (:type %) (:amount %)) transactions)]
    (reduce + amounts)))

(defn get-it [profile-id id]
  (db/get-record table id profile-id))

(defn save [transaction]
  (db/save-record table transaction))

(defn delete-it [profile-id id]
  (db/delete-record table id profile-id))