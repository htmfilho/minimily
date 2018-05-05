(ns minimily.auth.web.ui.signin
  (:require [hiccup.form                :refer [email-field form-to label 
                                                password-field hidden-field
                                                submit-button]]))

(defn signin-content []
  (form-to [:post "/account/login"]
        [:div {:class "page-title"} "Sign In"]
        [:div {:class "form-group"}
          (label "username" "Email address")
          (email-field {:class "form-control" :id "username" 
                        :autocomplete "off"} "username")]
        [:div {:class "form-group"}
          (label "password" "Password")
          (password-field {:class "form-control" :id "password" 
                           :autocomplete "off"} "password")]
        (hidden-field {:id "forward"} "forward" "/")
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"]))