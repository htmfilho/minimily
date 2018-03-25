(ns minimily.accounting.web.ctrl.transaction
  (:require [ring.util.response                          :refer [redirect]]
            [minimily.accounting.web.ui.transaction-form :refer [transaction-form-page]]
            [minimily.accounting.model.account           :as account-model]
            [minimily.accounting.model.transaction       :as transaction-model]))

(defn new-transaction [session account-id]
  (let [account (account-model/get-it account-id)]
    (transaction-form-page session account)))

(defn edit-transaction [session id]
  (let [transaction (transaction-model/get-it id)]
    (transaction-form-page session transaction)))

(defn save-transaction [transaction account-id]
  (println transaction)
  (transaction-model/save transaction)
  (redirect (str "/accounts/" account-id)))

(defn delete-transaction [params]
  (let [id (:id params)]
    (transaction-model/delete-it id)
    (redirect "/accounts")))