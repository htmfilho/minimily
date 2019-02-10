(ns minimily.auth.web.ui.password
  (:require [hiccup.form                :refer :all]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn password-reset-request-page []
  (http-headers 
    (layout nil "Request to Reset Password"
      (form-to [:post "/account/pswd/reset/request"]
        [:p "Inform your email address. If we find it in our records, we will 
             send a message with instructions to reset the password."]
        [:div {:class "form-group"}
          (label "email" "Email address")
          (email-field {:class "form-control" :id "email" :maxlength "100"
                        :autocomplete "off"} "email")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/signin"} "Cancel"]))))

(defn password-reset-request-submitted-page [params]
  (http-headers
    (layout nil "Request to Reset Password Submitted"
      (form-to [:post "/account/pswd/reset/request/verify"]
        [:p (str "A message was sent to the informed email with a unique code 
                  generated exclusively for you and valid for a few minutes. 
                  Please, inform the code you received in the field below.")]
        [:div {:class "form-group"}
          (label "secret_code" "Secret Code")
          (text-field {:class "form-control" :id "secret_code"} "secret_code")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"]))))

(defn password-change-page [session]
  (http-headers 
    (layout nil "Changing Password"
      (form-to [:post "/account/pswd/change"]
        [:div {:class "row"}
          [:div {:class "col-md-5"}
            [:div {:class "form-group"}
              (label "password" "Password")
              (password-field {:class "form-control" :id "password" 
                              :autocomplete "off"} "password")]
            [:div {:class "form-group"}
              (label "password_confirm" "Password Confirmation")
              (password-field {:class "form-control" :id "password_confirm" 
                              :autocomplete "off"} "password_confirm")]]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"]))))

(defn password-change-confirmed-page []
  (http-headers 
    (layout nil "Password Changed !"
      [:a {:class "btn btn-secondary" :href "/signin"} "Sign In"])))