(ns minimily.accounting.web.api.account
  (:require [clojure.data.json                 :as json]
            [minimily.accounting.model.account :as account-model]))

(defn get-accounts [session params]
  (let [profile-id     (:profile-id session)
        third-party-id (:third_party params)]
    (json/write-str (if (empty? third-party-id)
                      (account-model/find-active-accounts profile-id)
                      (account-model/find-third-party-accounts profile-id third-party-id)))))

(defn get-currency [session account-id]
  (let [account (account-model/get-it account-id (:profile-id session))]
    (json/write-str (:currency account))))