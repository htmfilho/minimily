(ns minimily.auth.model.user-account
  (:require [hugsql.core              :as hugsql]
            [buddy.hashers            :as hashers]
            [minimily.utils.database  :as db]
            [minimily.utils.messenger :as messenger]))

(hugsql/def-sqlvec-fns "minimily/auth/model/sql/user_account.sql")

(def table :user_account)

(defn save [user-account]
  (db/save-record table user-account))

(defn authenticate [username password]
  (let [user-account (db/find-record (find-by-username-sqlvec {:username username}))]
    (if (and user-account (hashers/check password (:password user-account)))
      (db/find-record (authenticated-user-profile-sqlvec {:username username}))
      nil)))

(defn find-by-username [username]
  (db/find-record (find-by-username-sqlvec {:username username})))

(defn find-by-verification [verification]
  (db/find-record (find-by-verification-sqlvec {:verification verification})))

(defn set-verification [id uuid]
  (db/update-record table {:id id :verification uuid}))

(defn reset-verification [id]
  (set-verification id nil))

(defn set-new-password [id new-password]
  (let [hashed-password (hashers/derive new-password)]
    (db/update-record table {:id id :password hashed-password})))

(defn generate-uuid []
  (str (java.util.UUID/randomUUID)))

(defn send-request-reset [email uuid]
  (messenger/send-message email 
                          "Minimily Reset Password" 
                          (str uuid)))