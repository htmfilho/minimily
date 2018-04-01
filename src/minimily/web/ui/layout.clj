(ns minimily.web.ui.layout
  (:require [hiccup.page :as page]))

(defn layout [session title & content]
  (page/html5 {:lang "en"}
    [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" 
              :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
      [:title "Minimily"]
      (page/include-css "/css/bootstrap.min.css"
                        "/css/custom.css")
      [:script {:src "https://use.fontawesome.com/releases/v5.0.9/js/all.js" 
                :integrity "sha384-8iPTk2s/jMVj81dnzb/iFR2sdA7u06vHJyyLlAd4snFpCl/SnyUjRrbdJsw1pGIl" 
                :crossorigin "anonymous"}]]
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
        (when title [:div {:class "page-title"} title])
        content
        [:br]
        [:br]
        [:br]]

      [:nav {:class "navbar fixed-bottom navbar-light bg-light"}
        [:a {:class "nav-link" :href "http://www.minimily.com"} "Help"]]
      
      (page/include-js "/js/jquery-3.2.1.slim.min.js"
                       "/js/bootstrap.bundle.min.js"
                       "/js/minimily.js")]))
