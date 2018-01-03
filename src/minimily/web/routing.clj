(ns minimily.web.routing
  (:require [compojure.core            :as core]
            [compojure.route           :as route]
            [clojure.java.io           :as io]
            [minimily.auth.web.routing :as auth]
            [minimily.web.ui.home      :as ui]))

(defn routes []
  (core/routes
    (core/GET "/" [] (ui/home))))

(core/defroutes app
  (routes)
  (auth/routes)
  (route/resources "/")
  (core/ANY "*" [] (route/not-found (slurp (io/resource "public/404.html")))))