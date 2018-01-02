(ns minimily.web.ui.home
  (:require [hiccup.page :as page]
            [minimily.web.ui.layout :refer [layout]]))

(defn draw-ui []
  (layout "Home"
      [:a {:href "/new_account"} "New account"]))

(defn home []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (draw-ui)})