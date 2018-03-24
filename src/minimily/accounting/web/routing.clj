(ns minimily.accounting.web.routing
  (:require [ring.util.response                      :refer [redirect]]
            [compojure.core                          :as core]
            [minimily.accounting.web.ui.accounts     :refer [accounts-page]]
            [minimily.accounting.web.ui.account-form :refer [account-form-page]]
            [minimily.accounting.web.ui.account      :refer [account-page]]
            [minimily.accounting.model.account       :as account-model]))

(defn view-accounts [session]
  (let [accounts (account-model/find-all (:user-id session))]
    (accounts-page session accounts)))

(defn view-account [id]
  (let [account (account-model/get-it id)]
    (account-page account)))

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

(defn routes []
  (core/routes
    (core/GET  "/accounts"          {session :session} (view-accounts session))
    (core/GET  "/accounts/new"      {session :session} (new-account session))
    (core/POST "/accounts/save"     {session :session params :params} 
                                    (save-account session params))
    (core/GET  "/accounts/:id"      [id] (view-account id))
    (core/GET  "/accounts/:id/edit" [session id] (edit-account session id))
    (core/POST "/accounts/delete"   {params :params} (delete-account params))))