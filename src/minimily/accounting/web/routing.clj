(ns minimily.accounting.web.routing
  (:require [compojure.core                              :as core]
            [minimily.accounting.web.ctrl.account     :as account-ctrl]
            [minimily.accounting.web.ctrl.transaction :as transaction-ctrl]
            [minimily.accounting.web.ctrl.transfer    :as transfer-ctrl]))

(defn routes []
  (core/routes
    (core/context "/accounts" []
      (core/GET  "/"         {session :session} 
                             (account-ctrl/view-accounts session))
      (core/GET  "/new"      {session :session} 
                             (account-ctrl/new-account session))
      (core/POST "/save"     {session :session params :params} 
                             (account-ctrl/save-account session params))
      (core/POST "/delete"   {session :session params :params} 
                             (account-ctrl/delete-account session params))
      (core/GET  "/:id"      {session :session {id :id} :params}
                             (account-ctrl/view-account session id))
      (core/GET  "/:id/edit" {session :session {id :id} :params}
                             (account-ctrl/edit-account session id))
      
      (core/context "/:account/transfer" []
        (core/GET  "/"        {session :session {account :account} :params}
                              (transfer-ctrl/new-transfer session account))
        (core/POST "/perform" {session :session params :params}
                              (transfer-ctrl/perform-transfer session params)))

      (core/context "/:account/transactions" []
        (core/GET  "/new"      {session :session {account :account} :params}
                               (transaction-ctrl/new-transaction session account))
        (core/POST "/add"      {session :session params :params}
                               (transaction-ctrl/add-transaction session params))
        (core/POST "/save"     {session :session params :params}
                               (transaction-ctrl/save-transaction session params))
        (core/POST "/delete"   {session :session params :params}
                               (transaction-ctrl/delete-transaction session params))
        (core/GET  "/:id"      {session :session {account :account id :id} :params}
                               (transaction-ctrl/view-transaction session account id))
        (core/GET  "/:id/edit" {session :session {account :account id :id} :params}
                               (transaction-ctrl/edit-transaction session account id))))))