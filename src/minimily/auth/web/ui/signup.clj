(ns minimily.auth.web.ui.signup
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [hiccup.form                :refer [email-field form-to label 
                                                password-field submit-button
                                                text-field]]))

(defn signup-page
  ([] (signup-page nil nil))
  ([user-account message]
    (http-headers 
      (layout nil "Sign Up"
        (form-to [:post "/account/new"]
          (when message [:div {:class "alert alert-warning" :role "alert"} message])
          
          [:div.row
            [:div.col-md-6
              [:div.form-group
                (label "first-name" "First Name")
                (text-field {:class "form-control" :id "first-name" :required "required" :value (:first-name user-account)} "first-name")]]
            [:div.col-md-6
              [:div.form-group
                (label "last-name" "Last Name")
                (text-field {:class "form-control" :id "last-name" :required "required" :value (:last-name user-account)} "last-name")]]]

          [:div.row
            [:div.col-md-6
              [:div.form-group
                (label "email" "Email Address")
                (email-field {:class "form-control" :id "email" 
                              :autocomplete "off" :required "required" :value (:email user-account)} "email")]]
            [:div.col-md-6
              [:div.form-group
                (label "email-confirmation" "Email Confirmation")
                (email-field {:class "form-control" :id "email-confirmation" 
                              :autocomplete "off" :required "required" :value (:email-confirmation user-account)} "email-confirmation")]]]

          [:div.row
            [:div.col-md-6
              [:div.form-group
                (label "password" "Password")
                (password-field {:class "form-control" :id "password" 
                                 :required "required"} "password")]]
            [:div.col-md-6
              [:div.form-group
                (label "password-confirmation" "Password Confirmation")
                (password-field {:class "form-control" :id "password-confirmation" 
                                 :required "required"} "password-confirmation")]]]
          
          (submit-button {:class "btn btn-primary"} "Submit")
          (str "&nbsp;")
          [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"])))))