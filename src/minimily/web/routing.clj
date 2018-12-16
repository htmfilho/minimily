(ns minimily.web.routing
  (:require [compojure.core                  :as core]
            [compojure.route                 :as route]
            [clojure.java.io                 :as io]
            [minimily.auth.web.routing       :as auth]
            [minimily.accounting.web.routing :as accounting]
            [minimily.documents.web.routing  :as documents]
            [minimily.inventory.web.routing  :as inventory]
            [minimily.web.ui.home            :refer [home-page]]))

(defn routes []
  (core/routes
    (core/GET "/" {session :session} (home-page session))))

(core/defroutes app
  "3. Group all routes from all modules."
  (routes)
  (auth/routes)
  (accounting/routes)
  (documents/routes)
  (inventory/routes)
  (route/resources "/")
  (core/ANY "*" [] (route/not-found (slurp (io/resource "public/404.html")))))