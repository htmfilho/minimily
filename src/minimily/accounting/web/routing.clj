(ns minimily.accounting.web.routing
  (:require [compojure.core                              :as core]
            [minimily.accounting.web.ctrl.account     :as account-ctrl]
            [minimily.accounting.web.ctrl.transaction :as transaction-ctrl]))

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
                                   
      (core/context "/:id/transactions" []
        (core/GET  "/new"      {session :session} 
                              (transaction-ctrl/new-transaction session))
        (core/POST "/save"     {session :session params :params} 
                              (transaction-ctrl/save-transaction session params))
        (core/GET  "/:id"      [id] 
                              (transaction-ctrl/view-transaction id))
        (core/GET  "/:id/edit" [session id] 
                              (transaction-ctrl/edit-transaction session id))
        (core/POST "/delete"   {params :params} 
                              (transaction-ctrl/delete-transaction params))))))