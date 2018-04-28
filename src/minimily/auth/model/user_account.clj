(ns minimily.auth.model.user-account
  (:require [hugsql.core             :as hugsql]
            [minimily.utils.database :as db]))

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
