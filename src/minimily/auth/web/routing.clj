(ns minimily.auth.web.routing
  (:require [ring.util.response               :refer [redirect]]
            [compojure.core                   :as core]
            [minimily.auth.web.ui.signup      :refer [signup]]
            [minimily.auth.web.ui.signin      :refer [signin]]
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

(defn login [params session]
  (let [auth-user (user-account/authenticate (:username params) 
                                             (:password params))]
    (if (nil? auth-user)
      (println "login error")
      (let [session {:full-name (user-profile/full-name auth-user)
                     :user-id (:id auth-user)}]
        (-> (redirect "/")
            (assoc :session session))))))

(defn logout [session]
  (-> (redirect "/")
      (assoc :session {})))

(defn routes []
  (core/routes
    (core/GET  "/signin"      [] (signin))
    (core/GET  "/signup"      [] (signup))
    (core/GET  "/signout"     {session :session} (logout session))
    (core/POST "/account/new" {params :params} (new-account params))
    (core/POST "/account/login" {params :params session :session} (login params session))))