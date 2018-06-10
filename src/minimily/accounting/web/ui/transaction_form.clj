(ns minimily.accounting.web.ui.transaction-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field
                                                radio-button]]
            [minimily.utils.date        :refer [to-string today]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transaction-form-add [session account]
  (http-headers
    (layout session "Transaction"
      (form-to [:post (str "/accounts/" (:id account) "/transactions/add")]
        [:div {:class "row"}
          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "acc" "Account")
              [:br]
              [:span {:id "acc" :class "read-only"} (:name account)]]]
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "balance" "Balance")
              [:br]
              [:span {:id "balance" :class "read-only"} (:balance account)]]]]
        [:div {:class "form-group"}
          (label "type" "Type")
          [:br]
          [:div {:class "form-check form-check-inline"}
            (radio-button {:class "form-check-input" :id "credit"}
                          "type"
                          false
                          1)
            [:span {:class "form-check-label"} "Credit"]]
          [:div {:class "form-check form-check-inline"}
            (radio-button {:class "form-check-input" :id "debit"}
                          "type"
                          false
                          -1)
            [:span {:class "form-check-label"} "Debit"]]]
        [:div {:class "row"}
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "amount" "Amount")
              (text-field {:class "form-control" :id "amount"} 
                          "amount")]]
          [:div {:class "col-md-7"}
            [:div {:class "form-group"}
              (label "description" "Description")
              (text-field {:class "form-control" :id "description"} 
                          "description")]]
          [:div {:class "col-md-2"}
           [:div {:class "form-group"}
            (label "date_transaction" "Date")
            [:input {:type "date" :id "date_transaction" :name "date_transaction" :class "form-control"
                     :value (to-string (today) "yyyy-MM-dd")}]]]]
               
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))

(defn transaction-form-edit [session account transaction]
  (http-headers
    (layout session "Transaction"
      (form-to [:post (str "/accounts/" (:id account) "/transactions/save")]
        (hidden-field "id" (:id transaction))
        (show-field "Account" account :name)
        [:div {:class "row"}
          [:div {:class "col-md-1"}
            [:div {:class "form-group"}
              (show-field "Type" transaction :type "Credit" "Debit")]]
          [:div {:class "col-md-2"}
            (show-field "Amount" transaction :amount)]
          [:div {:class "col-md-7"}
            [:div {:class "form-group"}
              (label "description" "Description")
              (text-field {:class "form-control" :id "description"} 
                          "description" 
                          (:description transaction))]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "date_transaction" "Date")
              [:input {:type "date" :id "date_transaction" :name "date_transaction" :class "form-control"
                       :value (to-string (:date_transaction transaction) "yyyy-MM-dd")}]]]]
               
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))