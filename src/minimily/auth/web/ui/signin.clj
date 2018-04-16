(ns minimily.auth.web.ui.signin
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [hiccup.form                :refer [email-field form-to label 
                                                password-field submit-button]]))

(defn signin-content []
  (form-to [:post "/account/login"]
        [:div {:class "form-group"}
          (label "username" "Email address")
          (email-field {:class "form-control" :id "username" 
                        :autocomplete "off"} "username")]
        [:div {:class "form-group"}
          (label "password" "Password")
          (password-field {:class "form-control" :id "password" 
                           :autocomplete "off"} "password")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"]))

(defn signin-page []
  (http-headers 
    (layout nil "Sign In"
      (signin-content))))