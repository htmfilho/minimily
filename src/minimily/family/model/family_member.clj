(ns minimily.family.model.family-member
  (:require [minimily.utils.database :as db]))

(defn find-organizers-same-family [profile-id]
  (db/find-records ["select * 
                     from family_member 
                     where organizer is true and 
                           family in (select family 
                                      from family_member 
                                      where user_profile = ?)" profile-id]))

(defn list-family-organizers [profile-id]
  (let [organizers (find-organizers-same-family profile-id)]
    (str (if (empty? organizers)
            profile-id
            (reduce #(str %1 "," %2)
                    (map #(:user_profile %) organizers))))))