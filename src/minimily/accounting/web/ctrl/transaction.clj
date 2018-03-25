(ns minimily.accounting.web.ctrl.transaction
  (:require [ring.util.response                          :refer [redirect]]
            [minimily.accounting.web.ui.transaction-form :refer [transaction-form-page]]
            [minimily.accounting.web.ui.transaction      :refer [transaction-page]]
            [minimily.accounting.model.transaction       :as transaction-model]))

(defn view-transaction [id]
  (let [transaction (transaction-model/get-it id)]
    (transaction-page transaction)))

(defn new-transaction [session]
  (transaction-form-page session))

(defn edit-transaction [session id]
  (let [transaction (transaction-model/get-it id)]
    (transaction-form-page session transaction)))

(defn save-transaction [params]
  (let [transaction params)
        id (transaction-model/save transaction)]
    (redirect (str "/transactions/" id))))

(defn delete-transaction [params]
  (let [id (:id params)]
    (transaction-model/delete-it id)
    (redirect "/accounts")))