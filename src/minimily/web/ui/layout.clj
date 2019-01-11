(ns minimily.web.ui.layout
  (:require [hiccup.page                 :as page]
            [minimily.auth.web.ui.signin :as signin]
            [minimily.web.model.menu     :refer [menu-items]]))

(defn menu-bar-template [menu-item]
  (if (contains? menu-item :submenu)
    [:li {:class "nav-item dropdown"}
         [:a {:class "nav-link dropdown-toggle" :href (:link menu-item) :id "navbarDropdown" 
              :role "button" :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"}
          (:label menu-item)]
         [:div {:class "dropdown-menu" :aria-labelledby "navbarDropdown"}
          (map #(vector :a {:class "dropdown-item" :href (:link %)} (:label %)) 
               (:submenu menu-item))]]
    [:li {:class "nav-item"}
         [:a {:class "nav-link" :href (:link menu-item)} (:label menu-item)]]))

(defn layout [session title & content]
  (page/html5 {:lang "en"}
    [:head
      [:link {:rel "apple-touch-icon" :sizes "152x152" :href "/apple-touch-icon.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
      [:link {:rel "manifest" :href "/site.webmanifest"}]
      [:link {:rel "mask-icon" :href "/safari-pinned-tab.svg" :color "#5bbad5"}]

      [:meta {:name "msapplication-TileColor" :content "#ffc40d"}]
      [:meta {:name "theme-color" :content "#ffc40d"}]
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]

      [:title "Minimily"]
      (page/include-css "/css/bootstrap.min.css"
                        "/css/custom.css")
      [:link {:href "https://fonts.googleapis.com/css?family=Caveat|Montserrat|Pacifico" :rel "stylesheet"}]
      [:script {:src "https://use.fontawesome.com/releases/v5.2.0/js/all.js" 
                :integrity "sha384-4oV5EgaV02iISL2ban6c/RmotsABqE4yZxZLcYMAdG7FAPsyHYAPpywE9PJo+Khy" 
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
            (if (not (empty? session))
              [:ul {:class "navbar-nav mr-auto"}
                (map menu-bar-template menu-items)]
              [:ul {:class "navbar-nav mr-auto"}])]
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
        [:div {:class "page-title"} title]
        content
        [:br]
        [:br]
        [:br]]

      [:nav {:class "navbar fixed-bottom navbar-light bg-light"}
        [:a {:class "nav-link" :href "http://www.minimily.com"} "Help"]]
      
      (page/include-js "/js/jquery-3.3.1.min.js"
                       "/js/bootstrap.bundle.min.js"
                       "/js/echarts.min.js"
                       "/js/minimily.js"
                       "/js/minimily-accounting.js")]))
