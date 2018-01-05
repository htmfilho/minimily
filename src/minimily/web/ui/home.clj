(ns minimily.web.ui.home
  (:require [minimily.web.ui.layout :refer [layout]]))

(defn draw-ui [session]
  (layout session (:full-name session) (:user-id session)))

(defn home [session]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (draw-ui session)})