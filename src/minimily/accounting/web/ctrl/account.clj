(ns minimily.accounting.web.ctrl.account
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.accounting.web.ui.accounts     :refer [accounts-page]]
            [minimily.accounting.web.ui.account-form :refer [account-form-page]]
            [minimily.accounting.web.ui.account      :refer [account-page]]
            [minimily.accounting.model.account       :as account-model]
            [minimily.accounting.model.transaction   :as transaction-model]
            [minimily.accounting.model.currency      :as currency-model]))

(defn view-accounts [session]
  (let [active-accounts   (account-model/find-actives (:user-id session))
        inactive-accounts (account-model/find-inactives (:user-id session))
        currencies        (currency-model/find-all)]
    (accounts-page session active-accounts inactive-accounts currencies)))

(defn view-account [session id]
  (let [account (account-model/get-it (:user-id session) id)
        transactions (transaction-model/find-by-account (:user-id session) id)]
    (account-page session account transactions)))

(defn new-account [session]
  (let [currencies (currency-model/find-all)]
    (account-form-page session currencies)))

(defn edit-account [session id]
  (let [account    (account-model/get-it (:user-id session) id)
        currencies (currency-model/find-all)]
    (account-form-page session currencies account)))

(defn save-account [session params]
  (let [account (assoc params :holder (:user-id session))
        account (if (contains? account :active)
                  (assoc account :active true)
                  (assoc account :active false))
        id      (account-model/save account)]
    (redirect (str "/accounts/" id))))

(defn delete-account [session params]
  (let [id (:id params)]
    (account-model/delete-it (:user-id session) id)
    (redirect "/accounts")))