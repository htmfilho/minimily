(ns minimily.auth.web.ui.password
  (:require [hiccup.form                :refer :all]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn password-reset-request-page [message]
  (http-headers 
    (layout nil "Request to Reset Password"
      (form-to [:post "/account/pswd/reset/request"]
        (when message [:div {:class "alert alert-warning" :role "alert"} message])
        [:p "Inform your email address. If we find it in our records, we will 
             send a message with instructions to reset the password."]
        [:div {:class "form-group"}
          (label "email" "Email address")
          (email-field {:class "form-control" :id "email" :maxlength "100"
                        :autocomplete "off"} "email")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/signin"} "Cancel"]))))

(defn password-reset-request-submitted-page [params message]
  (http-headers
    (layout nil "Request to Reset Password Submitted"
      (form-to [:post "/account/pswd/reset/request/verify"]
        (when message [:div {:class "alert alert-warning" :role "alert"} message])
        [:p (str "Check your email for a link to reset your password. If it doesn’t appear within a few minutes, check your spam folder.")]
        [:div {:class "form-group"}
          (label "recovery-code" "Recovery Code")
          (text-field {:class "form-control" :id "recovery-code"} "recovery-code")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/signin"} "Cancel"]))))

(defn password-change-page [session message]
  (http-headers 
    (layout session "Changing Password"
      (form-to [:post "/account/pswd/change"]
        (when message [:div {:class "alert alert-warning" :role "alert"} message])
        [:div {:class "row"}
          [:div {:class "col-md-5"}
            [:div {:class "form-group"}
              (label "name" "Name")
              [:p (:full-name session)]]
            [:div {:class "form-group"}
              (label "password" "New Password")
              (password-field {:class "form-control" :id "password" 
                              :autocomplete "off"} "password")]
            [:div {:class "form-group"}
              (label "password_confirm" "Password Confirmation")
              (password-field {:class "form-control" :id "password_confirm" 
                              :autocomplete "off"} "password_confirm")]]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/signin"} "Cancel"]))))

(defn password-change-confirmed-page [session]
  (http-headers 
    (layout session "Password Changed !"
      [:a {:href "/"} "Go to Homepage"])))