(ns minimily.auth.web.routing
  (:require [ring.util.response                  :refer [redirect]]
            [compojure.core                      :as core]
            [minimily.web.ui.layout              :refer [layout]]
            [minimily.utils.web.wrapper          :refer [http-headers]]
            [minimily.auth.web.ui.signup         :refer [signup-page]]
            [minimily.auth.web.ui.signin         :refer [signin-content]]
            [minimily.auth.model.user-account    :as user-account-model]
            [minimily.auth.model.user-profile    :as user-profile-model]
            [minimily.auth.web.ctrl.user-account :as user-account-ctrl]))

(defn new-account [params]
  (let [user-account-id (user-account-model/save {:username (:email params)
                                                  :password (:password params)})]
    (user-profile-model/save {:user_account user-account-id
                              :first_name   (:first_name params)
                              :last_name    (:last_name params)
                              :email        (:email params)})
    (redirect "/signin")))

(defn signin-page []
  (http-headers 
    (layout nil "Sign In"
      (signin-content))))

(defn signin [params session]
  (let [auth-user (user-account-model/authenticate (:username params) 
                                                   (:password params))]
    (if auth-user
      (let [session {:full-name (user-profile-model/full-name auth-user)
                     :user-id   (:id auth-user)}
            forward (:forward params)]
        (-> (redirect (if (or (.isEmpty forward) (.equals forward "/signin")) "/" forward))
            (assoc :session session))))))

(defn signin-fail []
  (redirect "/"))

(defn signout [session]
  (-> (redirect "/")
      (assoc :session nil)))

(defn routes []
  (core/routes
    (core/GET  "/signin"                            [] (signin-page))
    (core/GET  "/signup"                            [] (signup-page))
    (core/GET  "/signout"                           {session :session} 
                                                    (signout session))
    (core/POST "/account/login"                     {params :params session :session}
                                                    (signin params session))
    (core/GET  "/account/login/fail"                [] 
                                                    (signin-fail))
    (core/GET  "/account/pswd/reset/request"        {session :session}
                                                    (user-account-ctrl/request-change-password session))
    (core/GET  "/account/pswd/reset/request/verify" {params :params session :session}
                                                    (user-account-ctrl/verify-request-change-password params session))
    (core/GET  "/account/pswd/change"               {params :params session :session}
                                                    (user-account-ctrl/change-password params session))
    (core/GET  "/account/pswd/change/confirmation"  {session :session}
                                                    (user-account-ctrl/confirm-change-password session))
    (core/POST "/account/new"                       {params :params} 
                                                    (new-account params))
    (core/GET  "/request"                           req (str req))))