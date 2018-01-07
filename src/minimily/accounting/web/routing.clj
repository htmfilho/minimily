(ns minimily.accounting.web.routing
  (:require [ring.util.response                      :refer [redirect]]
            [compojure.core                          :as core]
            [minimily.accounting.web.ui.accounts     :refer [accounts]]
            [minimily.accounting.web.ui.account-form :refer [account-form]]
            [minimily.accounting.model.account       :as account]))

(defn view-accounts [session]
  (let [user-accounts (account/find-all (:user-id session))]
    (accounts session user-accounts)))

(defn new-account []
  (account-form))

(defn save-account [session params]
  (let [account (assoc params :holder (:user-id session))]
    (println account)
    (str (account/save account))))

(defn routes []
  (core/routes
    (core/GET  "/accounts"      {session :session} (view-accounts session))
    (core/GET  "/accounts/new"  [] (new-account))
    (core/POST "/accounts/save" {session :session params :params} 
                                (save-account session params))))