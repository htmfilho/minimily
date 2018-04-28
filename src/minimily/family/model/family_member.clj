(ns minimily.family.model.family-member
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]
            [minimily.family.model.family-member :as family-member-model]))

(hugsql/def-sqlvec-fns "minimily/family/model/sql/family_member.sql")

(defn- find-organizers-same-family [profile-id]
  (db/find-records ["select * 
                     from family_member 
                     where organizer is true and 
                           family in (select family 
                                      from family_member 
                                      where user_profile = ?)" profile-id]))

(defn list-family-organizers [profile-id]
  (let [organizers (find-organizers-same-family profile-id)]
    (if (empty? organizers)
      profile-id
      (map #(:user_profile %) organizers))))