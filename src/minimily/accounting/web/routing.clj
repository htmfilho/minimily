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
      (core/POST "/delete"   {params :params} 
                             (account-ctrl/delete-account params))
      (core/GET  "/:id"      [id] 
                             (account-ctrl/view-account id))
      (core/GET  "/:id/edit" [session id] 
                             (account-ctrl/edit-account session id))
      
      (core/context "/:account/transfer" []
        (core/GET  "/"        {session :session {account :account} :params} 
                              (transfer-ctrl/new-transfer session account))
        (core/POST "/perform" {params :params}
                              (transfer-ctrl/perform-transfer params)))

      (core/context "/:account/transactions" []
        (core/GET  "/new"      [session account]
                               (transaction-ctrl/new-transaction session account))
        (core/POST "/add"      {params :params}
                               (transaction-ctrl/add-transaction params))
        (core/POST "/save"     {params :params}
                               (transaction-ctrl/save-transaction params))
        (core/POST "/delete"   {params :params}
                               (transaction-ctrl/delete-transaction params))
        (core/GET  "/:id"      [account id]
                               (transaction-ctrl/view-transaction account id))
        (core/GET  "/:id/edit" [session account id]
                               (transaction-ctrl/edit-transaction session account id))))))