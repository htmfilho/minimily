(ns minimily.auth.web.routing
  (:require [ring.util.response               :refer [redirect]]
            [compojure.core                   :as core]
            [minimily.web.ui.layout           :refer [layout]]
            [minimily.utils.web.wrapper       :refer [http-headers]]
            [minimily.auth.web.ui.signup      :refer [signup-page]]
            [minimily.auth.web.ui.signin      :refer [signin-content]]
            [minimily.auth.model.user-account :as user-account]
            [minimily.auth.model.user-profile :as user-profile]))

(defn new-account [params]
  (let [user-account-id (user-account/save {:username (:email params)
                                            :password (:password params)})]
    (user-profile/save {:user_account user-account-id
                        :first_name   (:first_name params)
                        :last_name    (:last_name params)
                        :email        (:email params)})
    (redirect "/signin")))

(defn signin-page []
  (http-headers 
    (layout nil "Sign In"
      (signin-content))))

(defn signin [params session]
  (let [auth-user (user-account/authenticate (:username params) 
                                             (:password params))]
    (if auth-user
      (let [session {:full-name (user-profile/full-name auth-user)
                     :user-id   (:id auth-user)}]
        (-> (redirect (:forward params))
            (assoc :session session))))))

(defn signin-fail []
  (redirect "/"))

(defn signout [session]
  (-> (redirect "/")
      (assoc :session {})))

(defn routes []
  (core/routes
    (core/GET  "/signin"        [] (signin-page))
    ;(core/GET  "/signup"        [] (signup-page))
    (core/GET  "/signout"       {session :session} (signout session))
    ;(core/POST "/account/new"   {params :params} (new-account params))
    (core/POST "/account/login" {params :params session :session} 
                                (signin params session))
    (core/GET  "/account/login/fail" [] (signin-fail))))