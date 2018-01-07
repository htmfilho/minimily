(ns minimily.accounting.web.ui.account-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-form-page [session]
  (http-headers
    (layout session "New Account"
      (form-to [:post "/accounts/save"]
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} "name")]
        [:div {:class "form-group"}
          (label "number" "Number")
          (text-field {:class "form-control" :id "number"} "number")]  
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/accounts"} "Cancel"]))))