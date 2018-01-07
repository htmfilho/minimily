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
  "Returns some reference data about the authenticated user or nil if the user
   is not authenticated."
  [username password]
  (let [auth_user (db/find-records
                    [(str "select p.id, first_name, last_name from "
                          (name table)
                          " u left join user_profile p" 
                          " on u.id = p.user_account"
                          " where u.username = ? and u.password = ?")
                     username password])]
    (if (empty? auth_user)
      nil
      (first auth_user))))
