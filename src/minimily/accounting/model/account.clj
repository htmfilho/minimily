(ns minimily.accounting.model.account
  (:require [minimily.persistence-model :refer :all]))

(defrecord Account [id holder name  ; required
                    number]         ; optional
  Entity

  (save [record]
    record)

  (remove [record]
    nil)

  (get-one [id]
    nil))
