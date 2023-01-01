(ns minimily.accounting.web.ui.account-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :as bootstrap]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-form-page [session currencies & [account]]
  (http-headers
    (layout session "Account"
      (form-to [:post "/accounting/accounts/save"]
        (when account (hidden-field "id" (:id account)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name" :maxlength "100"} 
                      "name" 
                      (:name account))]
        [:div {:class "row"}
          [:div {:class "col-md-4"}
            [:div {:class "form-group"}
              (label "currency" "Currency")
              [:select {:name "currency" :class "form-control" :id "currency"}
                       (map #(vector :option (if (:selected %) 
                                               {:value (:acronym %) :selected "true"}
                                               {:value (:acronym %)}) (str (:acronym %) " (" (:name %) ")")) currencies)]]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "debit_limit" "Debit Limit")
              (text-field {:class "form-control" :id "debit_limit"}
                          "debit_limit"
                          (:debit_limit account))]]
          [:div {:class "col-md-6"}
            (bootstrap/checkbox "active" "Active" (:active account))]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/accounts/" (:id account))} "Cancel"]))))