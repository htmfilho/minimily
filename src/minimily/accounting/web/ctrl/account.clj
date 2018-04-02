(ns minimily.accounting.web.ctrl.account
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.accounting.web.ui.accounts     :refer [accounts-page]]
            [minimily.accounting.web.ui.account-form :refer [account-form-page]]
            [minimily.accounting.web.ui.account      :refer [account-page]]
            [minimily.accounting.model.account       :as account-model]
            [minimily.accounting.model.transaction   :as transaction-model]))

(defn view-accounts [session]
  (let [accounts (account-model/find-all (:user-id session))]
    (accounts-page session accounts)))

(defn view-account [session id]
  (let [account (account-model/get-it id)
        transactions (transaction-model/find-by-account id)]
    (account-page session account transactions)))

(defn new-account [session]
  (account-form-page session))

(defn edit-account [session id]
  (let [account (account-model/get-it id)]
    (account-form-page session account)))

(defn save-account [session params]
  (let [account (assoc params :holder (:user-id session))
        id      (account-model/save account)]
    (redirect (str "/accounts/" id))))

(defn delete-account [params]
  (let [id (:id params)]
    (account-model/delete-it id)
    (redirect "/accounts")))