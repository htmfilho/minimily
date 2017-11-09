(ns minimily.web.ui.layout
  (:require [hiccup.page :as page]))

(defn layout [title content]
  (page/html5 
    [:head
      [:title "Minimily"]]
    [:body 
      [:h1 title]
      content]))
