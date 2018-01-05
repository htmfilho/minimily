(ns minimily.web.ui.layout
  (:require [hiccup.page :as page]))

(defn layout [session title content]
  (page/html5 
    [:head
      [:title "Minimily"]
      (page/include-css "/css/bootstrap.min.css"
                        "/css/custom.css")]
    [:body 
      [:nav {:class "navbar fixed-top navbar-dark bg-primary"}
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
          (if (empty? session)
            [:ul {:class "navbar-nav my-2 my-lg-0"}
              [:li {:class "nav-item"}
                [:a {:class "nav-link my-2 my-sm-0" :href "/signup"} "Sign Up"]]
              [:li {:class "nav-item"}
                [:a {:class "nav-link  my-2 my-sm-0" :href "/signin"} "Sign In"]]]
            [:ul {:class "navbar-nav my-2 my-lg-0"}
              [:li {:class "nav-item"}
                [:a {:class "nav-link  my-2 my-sm-0" :href "/signout"} "Sign Out"]]])]]
      
      [:div {:class "container"}
        (when title 
          [:div {:class "page-title"} title])
        content]
      
      (page/include-js "/js/jquery-3.2.1.slim.min.js"
                       "/js/popper.min.js"
                       "/js/bootstrap.min.js")]))
