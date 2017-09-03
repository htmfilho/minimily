(ns minimily.accounting.model.account
  (:require [minimily.utils.database :refer :all]))

(defrecord Account [id holder name  ; required
                    number])        ; optional
