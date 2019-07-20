(ns minimily.auth.web.routing
  (:require [ring.util.response                  :refer [redirect]]
            [compojure.core                      :as core]
            [minimily.auth.web.ui.signup         :refer [signup-page]]
            [minimily.auth.web.ctrl.user-account :as user-account-ctrl]))

(defn routes []
  (core/routes
    (core/GET "/signin"                            
              [] 
              (user-account-ctrl/signin-page))

    (core/GET "/signup"
              []
              (signup-page))

    (core/GET "/signout"
              {session :session} 
              (user-account-ctrl/signout session))
    
    (core/POST "/account/login"
               {params :params}
               (user-account-ctrl/signin params))
    
    (core/GET  "/account/pswd/reset/request"
               [] 
               (user-account-ctrl/request-reset-password))
    
    (core/POST "/account/pswd/reset/request"
               {params :params}
               (user-account-ctrl/send-request-reset-password params))
    
    (core/GET "/account/pswd/reset/request/verify" 
              {params :params}
              (user-account-ctrl/verify-request-reset-password params))
    
    (core/POST "/account/pswd/reset/request/verify" 
               {params :params}
               (user-account-ctrl/check-code-reset-password params))
    
    (core/GET "/account/pswd/change"
              {session :session}
              (user-account-ctrl/changing-password session))
    
    (core/POST "/account/pswd/change"
               {params :params session :session}
               (user-account-ctrl/change-password params session))
    
    (core/GET "/account/pswd/change/confirmation"
              {session :session}
              (user-account-ctrl/confirm-change-password session))
    
    (core/POST "/account/new"
               {params :params}
               (user-account-ctrl/new-account params))
    
    (core/GET "/request"
              req 
              (str req))))