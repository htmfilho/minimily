(ns minimily.accounting.web.api.transaction
  (:require [minimily.accounting.model.transaction :as transaction-model]
            [clojure.data.json                     :as json]
            [minimily.utils.date                   :as dt]))

(defn date-to-string [key val]
  (if (= key :date_transaction)
    (dt/to-string val "yyyy-MM-dd")
    val))

(defn balance-history [session account-id]
  (json/write-str (transaction-model/find-balance-history account-id)
                  :value-fn date-to-string))