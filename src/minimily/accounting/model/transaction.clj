(ns minimily.accounting.model.transaction
  (:require [minimily.utils.database :as db]))

(def table :transaction)

(defn find-by-account [account-id]
  (db/find-records (str "select * from transaction where account = " 
                        account-id)))

(defn get-it [id]
  (db/get-record table id))

(defn save [transaction]
  (let [transaction (conj transaction {:amount 1.2})
        transaction (conj transaction {:account (:account-id transaction)})
        transaction (dissoc transaction :account-id)]
    (println transaction)
    (db/save-record table transaction)))

(defn delete-it [id]
  (db/delete-record table id))