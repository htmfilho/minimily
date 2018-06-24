(ns minimily.accounting.web.api.account
  (:require [clojure.data.json                 :as json]
            [minimily.accounting.model.account :as account-model]))

(defn get-currency [session account-id]
  (let [account (account-model/get-it nil account-id)]
    (json/write-str (:currency account))))