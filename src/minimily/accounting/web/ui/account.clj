(ns minimily.accounting.web.ui.account
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout :refer [layout]]
            [minimily.web.ui.bootstrap :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-page [account & [transactions]]
  (http-headers
    (layout nil "Account"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/accounts/delete"]
            [:a {:href "/accounts" :class "btn btn-secondary"} "Back"]
            (str "&nbsp;")
            [:a {:href (str "/accounts/" (:id account) "/edit") 
                :class "btn btn-primary"} "Edit"]
            (str "&nbsp;")
            (hidden-field "id" (:id account))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          (show-field "Name"   account :name)
          (show-field "Number" account :number)]]
      [:br]
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:a {:href (str "/accounts/" (:id account) "/transactions/new") :class "btn btn-secondary"} "New Transaction"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Description"]
              [:th "Type"]
              [:th "Amount"]
              [:th "Date"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/transactions/" (:id %))} 
                                       (:description %)]]
                              [:td (:type %)]
                              [:td (:amount %)]
                              [:td (:date_transaction %)]) transactions)]]])))
