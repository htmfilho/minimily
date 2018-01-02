(ns minimily.web.ui.layout
  (:require [hiccup.page :as page]))

(defn layout [title content]
  (page/html5 
    [:head
      [:title "Minimily"]
      (page/include-css "/css/bootstrap.min.css")]
    [:body 
      [:nav {:class "navbar navbar-light bg-light"}
        [:a {:class "navbar-brand" :href "/"} "Minimily"]
        [:button {:class "navbar-toggler"
                  :type "button"
                  :data-toggle "collapse"
                  :data-target "#navbarSupportedContent"
                  :aria-controls "navbarSupportedContent"
                  :aria-expanded "false"
                  :aria-label "Toggle navigation"}
          [:span {:class "navbar-toggler-icon"}]]
        [:div {:class "collapse navbar-collapse" :id "navbarSupportedContent"}
          [:ul {:class "navbar-nav mr-auto"}]
          [:ul {:class "navbar-nav my-2 my-lg-0"}
            [:li {:class "nav-item"}
              [:a {:class "nav-link  my-2 my-sm-0" :href "/new_account"} "Sign Up"]]]]]
      
      [:div {:class "container"} 
        (when title [:h1 title])
        content]
      
      (page/include-js "/js/jquery-3.2.1.slim.min.js"
                       "/js/popper.min.js"
                       "/js/bootstrap.min.js")]))
