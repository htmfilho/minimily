(ns minimily.family.model.family
  (:require [hugsql.core                         :as hugsql]
            [minimily.utils.database             :as db]))

(hugsql/def-sqlvec-fns "minimily/family/model/sql/family.sql")

(defn find-family-organizer [profile-id]
  (first (db/find-records (family-of-organizer-sqlvec {:profile-id profile-id}))))
