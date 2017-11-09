(ns minimily.routing
  (:require [compojure.core            :as core]
            [compojure.route           :as route]
            [clojure.java.io           :as io]
            [minimily.auth.web.routing :as auth]))

(defn homepage []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<a href='/new_account'>New Account</a>"})

(core/defroutes app
  (core/GET "/" [] (homepage))
  (auth/routes)
  (core/ANY "*" [] (route/not-found (slurp (io/resource "public/404.html")))))