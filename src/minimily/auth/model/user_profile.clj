(ns minimily.auth.model.user-profile
  (:require [hugsql.core             :as hugsql]
            [minimily.utils.database :as db]))

(hugsql/def-sqlvec-fns "minimily/auth/model/sql/user_profile.sql")

(def table :user_profile)

(defn save [user-profile]
  (db/save-record table user-profile))

(defn full-name [profile]
  (str (:first_name profile)
       " "
       (:last_name profile)))

(defn find-profile-by-email [email]
  (db/find-record (profile-by-email-sqlvec {:email email})))