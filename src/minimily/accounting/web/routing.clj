(ns minimily.accounting.web.routing
  (:require [compojure.core                           :as url]
            [minimily.accounting.web.ctrl.account     :as account-ctrl]
            [minimily.accounting.web.ctrl.category    :as category-ctrl]
            [minimily.accounting.web.ctrl.transaction :as transaction-ctrl]
            [minimily.accounting.web.ctrl.transfer    :as transfer-ctrl]
            [minimily.accounting.web.ctrl.accounting  :as accounting-ctrl]
            [minimily.accounting.web.api.account      :as account-api]
            [minimily.accounting.web.api.transaction  :as transaction-api]))

(defn routes []
  (url/routes
    (url/context "/accounting" []
      (url/GET  "/"         {session :session} 
                             (accounting-ctrl/view-accounting session))

      (url/context "/accounts" []
        (url/GET  "/"         {session :session {currency :currency} :params}
                              (account-ctrl/view-accounts session))
        (url/GET  "/new"      {session :session} 
                              (account-ctrl/new-account session))
        (url/POST "/save"     {session :session params :params} 
                              (account-ctrl/save-account session params))
        (url/POST "/delete"   {session :session params :params} 
                              (account-ctrl/delete-account session params))
        (url/GET  "/:id"      {session :session {id :id} :params}
                              (account-ctrl/view-account session id))
        (url/GET  "/:id/edit" {session :session {id :id} :params}
                              (account-ctrl/edit-account session id))
        
        (url/context "/:account/transfer" []
          (url/GET  "/"        {session :session {account :account} :params}
                              (transfer-ctrl/new-transfer session account))
          (url/POST "/perform" {session :session params :params}
                              (transfer-ctrl/perform-transfer session params)))

        (url/context "/:account/transactions" []
          (url/GET  "/new"      {session :session {account :account} :params}
                                (transaction-ctrl/new-transaction session account))
          (url/POST "/add"      {session :session params :params}
                                (transaction-ctrl/add-transaction session params))
          (url/POST "/save"     {session :session params :params}
                                (transaction-ctrl/save-transaction session params))
          (url/POST "/delete"   {session :session params :params}
                                (transaction-ctrl/delete-transaction session params))
          (url/GET  "/:id"      {session :session {account :account id :id} :params}
                                (transaction-ctrl/view-transaction session account id))
          (url/GET  "/:id/edit" {session :session {account :account id :id} :params}
                                (transaction-ctrl/edit-transaction session account id))))

      (url/context "/categories" []
        (url/GET  "/"         {session :session} 
                              (category-ctrl/view-parent-categories session))
        (url/GET  "/new"      {session :session {parent :parent} :params} 
                              (category-ctrl/new-category session parent))
        (url/POST "/save"     {session :session params :params} 
                              (category-ctrl/save-category session params))
        (url/POST "/delete"   {session :session params :params} 
                              (category-ctrl/delete-category session params))
        (url/GET  "/:id"      {session :session {id :id} :params}
                              (category-ctrl/view-category session id))
        (url/GET  "/:id/edit" {session :session {id :id} :params}
                              (category-ctrl/edit-category session id)))
    
      (url/context "/api/accounts" []
        (url/GET "/:account/balance/history" 
                {session :session {account :account} :params}
                (transaction-api/get-balance-history session account))
        (url/GET "/:account/currency"
                {session :session {account :account} :params}
                (account-api/get-currency session account))))))