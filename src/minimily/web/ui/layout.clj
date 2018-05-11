(ns minimily.web.ui.layout
  (:require [hiccup.page                 :as page]
            [minimily.auth.web.ui.signin :as signin]))

(defn layout [session title & content]
  (page/html5 {:lang "en"}
    [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" 
              :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
      [:title "Minimily"]
      (page/include-css "/css/bootstrap.min.css"
                        "/css/custom.css")
      [:link {:href "https://fonts.googleapis.com/css?family=Caveat|Montserrat|Pacifico" :rel "stylesheet"}]
      [:script {:src "https://use.fontawesome.com/releases/v5.0.9/js/all.js" 
                :integrity "sha384-8iPTk2s/jMVj81dnzb/iFR2sdA7u06vHJyyLlAd4snFpCl/SnyUjRrbdJsw1pGIl" 
                :crossorigin "anonymous"}]]
    [:body 
      [:nav {:class "navbar navbar-expand-lg fixed-top navbar-dark bg-dark"}
        [:a {:class "navbar-brand" :href "/" :style "font-family: 'Pacifico', cursive;"} "Minimily"]
        [:button {:class "navbar-toggler"
                  :type "button"
                  :data-toggle "collapse"
                  :data-target "#navbarSupportedContent"
                  :aria-controls "navbarSupportedContent"
                  :aria-expanded "false"
                  :aria-label "Toggle navigation"}
          [:span {:class "navbar-toggler-icon"}]]
        [:div {:class "collapse navbar-collapse" :id "navbarSupportedContent"}
          [:div {:class "container"}
            [:ul {:class "navbar-nav mr-auto"}
              [:li {:class "nav-item"}
                [:a {:class "nav-link" :href "/accounts"} "Accounts"]]
              [:li {:class "nav-item"}
                [:a {:class "nav-link" :href "/folders"} "Documents"]]
              
              [:li {:class "nav-item dropdown"}
                [:a {:class "nav-link dropdown-toggle" :href "/inventory" :id "navbarDropdown" 
                     :role "button" :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"}
                  "Inventory"]
                [:div {:class "dropdown-menu" :aria-labelledby "navbarDropdown"}
                  [:a {:class "dropdown-item" :href "/inventory/goods"} "Goods"]
                  [:a {:class "dropdown-item" :href "/inventory/locations"} "Locations"]
                  [:a {:class "dropdown-item" :href "/inventory/collections"} "Collections"]]]]]
          (if (empty? session)
            [:ul {:class "navbar-nav my-2 my-lg-0"}
              ;[:li {:class "nav-item"}
              ;  [:a {:class "nav-link my-2 my-sm-0" :href "/signup"} "Sign Up"]]
              [:li {:class "nav-item"}
                [:a {:class "nav-link  my-2 my-sm-0" :href "/signin"} "Sign In"]]]
            [:ul {:class "navbar-nav my-2 my-lg-0"}
              [:li {:class "nav-item"}
                [:a {:class "nav-link  my-2 my-sm-0" :href "/signout"} "Sign Out"]]])]]
      
      [:div {:class "container"}
        (when (and title (not (empty? session))) 
          [:div {:class "page-title"} title])
        (if (empty? session)
          (signin/signin-content)
          content)
        [:br]
        [:br]
        [:br]]

      [:nav {:class "navbar fixed-bottom navbar-light bg-light"}
        [:a {:class "nav-link" :href "http://www.minimily.com"} "Help"]]
      
      (page/include-js "/js/jquery-3.3.1.slim.min.js"
                       "/js/bootstrap.bundle.min.js"
                       "/js/minimily.js")]))
