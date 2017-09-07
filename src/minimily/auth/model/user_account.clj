(ns minimily.auth.model.user-account
  (:require [minimily.utils.database :as db]))

(def table :user_account)

(defn get-it [id]
  (db/get-record table id))

(defn save [record]
  (db/save-record table record))

(defn delete [id]
  (db/delete-record table id))

(defn authenticate [username password]
  (let [auth_user (db/find-records [(str "select * from " 
                                      (name table) 
                                      " where username = ? and password = ?")
                                    username password])]
    (not (empty? authenticated_user))))
