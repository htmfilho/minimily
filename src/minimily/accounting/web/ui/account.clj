(ns minimily.accounting.web.ui.account
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field]]
            [minimily.utils.date        :refer [to-string]]
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
          [:div {:class "row"}
            [:div {:class "col-md-4"} (show-field "Name"    account :name)]
            [:div {:class "col-md-4"} (show-field "Number"  account :number)]
            [:div {:class "col-md-4"} (show-field "Balance" account :balance)]]]]
      [:br]
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:a {:href (str "/accounts/" (:id account) "/transactions/new") 
               :class "btn btn-secondary"} "New Transaction"]
          (str "&nbsp;")
          [:a {:href (str "/accounts/" (:id account) "/transfer") 
               :class "btn btn-secondary"} "New Transfer"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Description"]
              [:th "Type"]
              [:th "Amount"]
              [:th "Date"]
              [:th "Balance"]
              [:th ""]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounts/" (:id account) "/transactions/" (:id %))} 
                                       (:description %)]]
                              [:td (if (> (:type %) 0) "Credit" "Debit")]
                              [:td (if (< (:type %) 0) 
                                      [:span {:class "debit"} (:amount %)]
                                      [:span {:class "credit"} (:amount %)])]
                              [:td (to-string (:date_transaction %) "MMM dd, yyyy - HH:mm")]
                              [:td (:balance %)]
                              [:td (when (:account_transfer %) [:a {:href (str "/accounts/" (:account_transfer %))} [:i {:class "fas fa-link"}]])]) transactions)]]])))
