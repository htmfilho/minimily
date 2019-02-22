(ns minimily.auth.model.user-account
  (:require [hugsql.core              :as hugsql]
            [minimily.utils.database  :as db]
            [minimily.utils.messenger :as messenger]))

(hugsql/def-sqlvec-fns "minimily/auth/model/sql/user_account.sql")

(def table :user_account)

(defn save [user-account]
  (db/save-record table user-account))

(defn authenticate [username password]
  (let [auth_user (db/find-records
                    (authenticated-user-profile-sqlvec {:username username :password password}))]
    (if (empty? auth_user)
      nil
      (first auth_user))))

(defn existing-user-account [username]
  (db/find-records (existing-username-sqlvec {:username username})))

(defn set-verification [id uuid]
  (db/update-record table {:id id :verification uuid}))

(defn generate-uuid []
  (str (java.util.UUID/randomUUID)))

(defn send-request-reset [email uuid]
  (messenger/send-message email 
                          "Minimily Reset Password" 
                          (str uuid)))