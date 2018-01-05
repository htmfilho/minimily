(ns minimily.auth.web.ui.signup
  (:require [hiccup.form            :refer [email-field form-to label 
                                            password-field submit-button
                                            text-field]]
            [minimily.web.ui.layout :refer [layout]]))

(defn signup-page []
  (layout nil "Sign Up"
      (form-to [:post "/account/new"]
        [:div {:class "form-group"}
          (label "first_name" "First Name")
          (text-field {:class "form-control" :id "first_name"} "first_name")]
        [:div {:class "form-group"}
          (label "last_name" "Last Name")
          (text-field {:class "form-control" :id "last_name"} "last_name")]  
        [:div {:class "form-group"}
          (label "email" "Email address")
          (email-field {:class "form-control" :id "email" 
                        :autocomplete "off"} "email")]
        [:div {:class "form-group"}
          (label "password" "Password")
          (password-field {:class "form-control" :id "password" 
                           :autocomplete "off"} "password")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"])))

(defn signup []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (signup-page)})