(ns minimily.accounting.web.ui.account-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-form-page [session & [account]]
  (http-headers
    (layout session "Account"
      (form-to [:post "/accounts/save"]
        (when account (hidden-field "id" (:id account)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name account))]
        [:div {:class "form-group"}
          (label "number" "Number")
          (text-field {:class "form-control" :id "number"} 
                      "number" 
                      (:number account))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href "/accounts"} "Cancel"]))))