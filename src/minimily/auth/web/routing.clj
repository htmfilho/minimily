(ns minimily.auth.web.routing
  (:require [compojure.core :as core]
            [minimily.auth.web.ui.new-account :as ui]))

(defn routes []
  (core/routes
    (core/GET "/new_account" [] (ui/new-account))))