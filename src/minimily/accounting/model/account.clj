(ns minimily.accounting.model.account
  (:require [minimily.persistence-model :refer :all]
            [honeysql.core              :as sql]
            [honeysql.helpers           :refer :all :as helpers]))

(defrecord Account [id holder name  ; required
                    number]         ; optional
  Entity

  (defn save [record]
    record)

  (defn remove [record]
    nil)

  (defn get-one [id]
    (sql/build :select :*
               :from :account
               :where [:= :id id]))
