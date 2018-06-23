(ns minimily.accounting.web.ui.account-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-form-page [session currencies & [account]]
  (http-headers
    (layout session "Account"
      (form-to [:post "/accounts/save"]
        (when account (hidden-field "id" (:id account)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name account))]
        [:div {:class "row"}
         [:div {:class "col-md-6"}
          [:div {:class "form-group"}
           (label "number" "Number")
           (text-field {:class "form-control" :id "number"}
                       "number"
                       (:number account))]]
         [:div {:class "col-md-6"}
          [:div {:class "form-group"}
           (label "currency" "Currency")
           [:select {:name "currency" :class "form-control" :id "currency"}
             (map #(vector :option {:value (:acronym %)} (str (:acronym %) " (" (:name %) ")")) currencies)]]]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))