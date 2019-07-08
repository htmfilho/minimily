(ns minimily.auth.model.user-account
  (:require [hugsql.core              :as hugsql]
            [minimily.utils.database  :as db]
            [minimily.utils.messenger :as messenger]))

(hugsql/def-sqlvec-fns "minimily/auth/model/sql/user_account.sql")

(def table :user_account)

(defn save [user-account]
  (db/save-record table user-account))

(defn authenticate [username password]
  (first (db/find-records (authenticated-user-profile-sqlvec {:username username 
                                                              :password password}))))

(defn find-by-username [username]
  (first (db/find-records (find-by-username-sqlvec {:username username}))))

(defn find-by-verification [verification]
  (first (db/find-records (find-by-verification-sqlvec {:verification verification}))))

(defn set-verification [id uuid]
  (db/update-record table {:id id :verification uuid}))

(defn reset-verification [id]
  (set-verification id nil))

(defn set-new-password [id new-password]
  (db/update-record table {:id id :password new-password}))

(defn generate-uuid []
  (str (java.util.UUID/randomUUID)))

(defn send-request-reset [email uuid]
  (messenger/send-message email 
                          "Minimily Reset Password" 
                          (str uuid)))