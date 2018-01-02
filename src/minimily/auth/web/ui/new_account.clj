(ns minimily.auth.web.ui.new-account
  (:require [hiccup.form            :as form]
            [minimily.web.ui.layout :refer [layout]]))

(defn draw-ui []
  (layout "New Account"
      (form/form-to [:post "/account/new"])))

(defn new-account []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (draw-ui)})