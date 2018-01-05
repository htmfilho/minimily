(ns minimily.web.ui.home
  (:require [minimily.web.ui.layout :refer [layout]]))

(defn draw-ui [session]
  (layout (:username session) nil))

(defn home [session]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (draw-ui session)})