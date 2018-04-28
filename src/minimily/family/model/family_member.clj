(ns minimily.family.model.family-member
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]))

(hugsql/def-sqlvec-fns "minimily/family/model/sql/family_member.sql")

(defn- find-organizers-same-family [profile-id]
  (db/find-records (family-organizers-sqlvec {:profile-id profile-id})))

(defn list-family-organizers [profile-id]
  (let [organizers (find-organizers-same-family profile-id)]
    (if (empty? organizers)
      [profile-id]
      (map #(:user_profile %) organizers))))