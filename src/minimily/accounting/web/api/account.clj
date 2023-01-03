(ns minimily.accounting.web.api.account
  (:require [clojure.data.json                 :as json]
            [minimily.accounting.model.account :as account-model]))

(defn get-accounts [session params]
  (let [profile        (:profile-id session)
        third-party-id (:third_party params)]
    (json/write-str (if (empty? third-party-id)
                      (account-model/find-actives profile)
                      (account-model/find-third-party-accounts third-party-id)))))

(defn get-currency [session account-id]
  (let [account (account-model/get-it nil account-id)]
    (json/write-str (:currency account))))