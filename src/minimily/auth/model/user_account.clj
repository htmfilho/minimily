(ns minimily.auth.model.user-account
  (:require [minimily.utils.database :as db]))

(def table :user_account)

(defn get-it [id]
  (db/get-record table id))

(defn save [user-account]
  (db/save-record table user-account))

(defn delete [id]
  (db/delete-record table id))

(defn authenticate [username password]
  (let [auth_user (db/find-records
                    [(str "select * from "
                          (name table)
                          " where username = ? and password = ?")
                     username password])]
    (if (empty? auth_user)
      nil
      (dissoc (first auth_user) :password :registration_date))))
