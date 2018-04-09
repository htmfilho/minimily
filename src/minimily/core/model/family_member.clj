(ns minimily.core.model.family-member
  (:require [minimily.utils.database :as db]))

(defn find-members-same-family [profile-id]
  (db/find-records (str "select * 
                         from family_member 
                         where organizer is true and 
                               family in (select family 
                                          from family_member 
                                          where user_profile = " profile-id ")")))