(ns minimily.web.ui.home
  (:require [minimily.web.ui.layout :refer [layout]]))

(defn draw-ui []
  (layout "" ""))

(defn home []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (draw-ui)})