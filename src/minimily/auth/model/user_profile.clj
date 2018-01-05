(ns minimily.auth.model.user-profile
  (:require [minimily.utils.database :as db]))

(def table :user_profile)

(defn save [user-profile]
  (db/save-record table user-profile))

(defn full-name [profile]
  (str (:first_name profile)
       " "
       (:last_name profile)))