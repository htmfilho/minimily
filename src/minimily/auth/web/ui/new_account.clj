(ns minimily.auth.web.ui.new-account
  (:require [hiccup.page :as page]
            [minimily.web.ui.layout :refer [layout]]))

(defn draw-ui []
  (layout "New Account"
      [:p "Create a new account"]))

(defn new-account []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (draw-ui)})