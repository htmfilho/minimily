(ns minimily.auth.web.ui.signin
  (:require [hiccup.form :refer :all]))

(defn signin-content [message]
  (form-to [:post "/account/login"]
    (when message [:div {:class "alert alert-warning" :role "alert"} message])
    [:div {:class "row"}
      [:div {:class "col-md-5"}
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
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"]
        [:br][:br]
        [:p [:a {:href "/account/pswd/reset/request"} "Forgot Password?"]]]]))