(ns minimily.web.ui.layout
  (:require [hiccup.page     :as page]))

(defn layout [title content]
  (page/html5 
    [:head
      [:title "Minimily"]
      (page/include-css "/css/bootstrap.min.css")]
    [:body 
      [:h1 title]
      content
      (page/include-js "/js/jquery-3.2.1.slim.min.js"
                       "/js/popper.min.js"
                       "/js/bootstrap.min.js")]))
