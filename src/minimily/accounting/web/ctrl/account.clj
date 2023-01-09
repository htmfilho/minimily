(ns minimily.accounting.web.ctrl.account
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.accounting.web.ui.accounts     :refer [accounts-page]]
            [minimily.accounting.web.ui.account-form :refer [account-form-page]]
            [minimily.accounting.web.ui.account      :refer [account-page]]
            [minimily.accounting.model.account       :as account-model]
            [minimily.accounting.model.transaction   :as transaction-model]
            [minimily.accounting.model.currency      :as currency-model]
            [minimily.accounting.model.third-party   :as third-party-model]))

(defn view-accounts [session]
  (let [active-accounts   (account-model/find-actives (:profile-id session))
        inactive-accounts (account-model/find-inactives (:profile-id session))
        currencies        (currency-model/find-all)]
    (accounts-page session active-accounts inactive-accounts currencies)))

(defn view-account [session id]
  (let [account      (account-model/get-it id (:profile-id session))
        account      (assoc account 
                       :percentage-used-credit 
                       (account-model/percentage-used-credit account))
        third-party  (third-party-model/get-it (:third_party account))
        transactions (transaction-model/find-by-account (:profile-id session) id)]
    (account-page session account third-party transactions)))

(defn new-account [session]
  (let [currencies    (currency-model/find-all)
        third-parties (third-party-model/find-third-parties (:profile-id session))]
    (account-form-page session currencies third-parties)))

(defn edit-account [session id]
  (let [account       (account-model/get-it id (:profile-id session))
        third-parties (map #(if (= (:id %) (:third_party account))
                              (conj % {:selected true})
                              (conj % {:selected false})) 
                           (third-party-model/find-third-parties (:profile-id session)))
        currencies    (map #(if (= (:acronym %) (:currency account))
                              (conj % {:selected true})
                              (conj % {:selected false})) 
                           (currency-model/find-all))]
    (account-form-page session currencies third-parties account)))

(defn save-account [session params]
  (let [account (-> params 
                    (assoc :profile (:profile-id session))
                    (conj {:third_party (if (empty? (:third_party params)) 
                                           nil
                                           (Integer/parseInt (:third_party params)))})
                    (assoc :debit_limit (if (empty? (:debit_limit params))
                                            0
                                            (BigDecimal. (:debit_limit params))))
                    (assoc :active (contains? params :active)))
        id      (account-model/save account)]
    (redirect (str "/accounting/accounts/" id))))

(defn delete-account [session params]
  (let [id (Integer/parseInt (:id params))]
    (account-model/delete-it (:profile-id session) id)
    (redirect "/accounting/accounts")))