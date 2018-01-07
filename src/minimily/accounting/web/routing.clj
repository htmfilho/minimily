(ns minimily.accounting.web.routing
  (:require [ring.util.response                      :refer [redirect]]
            [compojure.core                          :as core]
            [minimily.accounting.web.ui.accounts     :refer [accounts-page]]
            [minimily.accounting.web.ui.account-form :refer [account-form-page]]
            [minimily.accounting.web.ui.account      :refer [account-page]]
            [minimily.accounting.model.account       :as account]))

(defn view-accounts [session]
  (let [accounts (account/find-all (:user-id session))]
    (accounts-page session accounts)))

(defn view-account [id]
  (let [acc (account/get-it id)]
    (account-page acc)))

(defn new-account [session]
  (account-form-page session))

(defn save-account [session params]
  (let [acc (assoc params :holder (:user-id session))
        id  (account/save acc)]
    (redirect (str "/accounts/" id))))

(defn delete-account [params]
  (let [id (:id params)]
    (account/delete-it id)
    (redirect "/accounts")))

(defn routes []
  (core/routes
    (core/GET  "/accounts"        {session :session} (view-accounts session))
    (core/GET  "/accounts/new"    {session :session} (new-account session))
    (core/POST "/accounts/save"   {session :session params :params} 
                                  (save-account session params))
    (core/GET  "/accounts/:id"    [id] (view-account id))
    (core/POST "/accounts/delete" {params :params} (delete-account params))))