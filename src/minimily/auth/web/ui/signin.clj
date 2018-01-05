(ns minimily.auth.web.ui.signin
  (:require [hiccup.form            :refer [email-field form-to label 
                                            password-field submit-button]]
            [minimily.web.ui.layout :refer [layout]]))

(defn signin-page []
  (layout "Sign In"
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
        [:a {:class "btn btn-outline-secondary" :href "/"} "Cancel"])))

(defn signin []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (signin-page)})