(ns minimily.family.web.ui.family-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :as bootstrap]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn family-form-page [session & [account]]
  (http-headers
    (layout session "Account"
      (form-to [:post "/accounts/save"]
        (when account (hidden-field "id" (:id account)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name" :maxlength "30"} 
                      "name" 
                      (:name account))]
        [:div {:class "row"}
          [:div {:class "col-md-4"}
            [:div {:class "form-group"}
              (label "number" "Number")
              (text-field {:class "form-control" :id "number" :maxlength "30"}
                          "number"
                          (:number account))]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "debit_limit" "Debit Limit")
              (text-field {:class "form-control" :id "debit_limit"}
                          "debit_limit"
                          (:debit_limit account))]]
          [:div {:class "col-md-2"}
            (bootstrap/checkbox "active" "Active" (:active account))]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))