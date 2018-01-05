(ns minimily.auth.model.user-account
  (:require [minimily.utils.database :as db]))

(def table :user_account)

(defn get-it [id]
  (db/get-record table id))

(defn save [user-account]
  (db/save-record table user-account))

(defn delete [id]
  (db/delete-record table id))

(defn authenticate
  "Returns full name."
  [username password]
  (let [auth_user (db/find-records
                    [(str "select first_name, last_name from "
                          (name table)
                          " left join user_profile on user_account.id = user_profile.user_account where username = ? and password = ?")
                     username password])]
    (if (empty? auth_user)
      nil
      (dissoc (first auth_user) :password :registration_date))))
