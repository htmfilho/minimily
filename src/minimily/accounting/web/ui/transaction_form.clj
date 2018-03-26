(ns minimily.accounting.web.ui.transaction-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field
                                                radio-button]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transaction-form-page [session account & [transaction]]
  (http-headers
    (layout session "Transaction"
      (form-to [:post (str "/accounts/" (:id account) "/transactions/save")]
        (when transaction (hidden-field "id" (:id transaction)))
        [:div {:class "form-group"}
          (label "acc" "Account")
          [:br]
          [:span {:id "acc" :class "read-only"} (:name account)]]
        [:div {:class "row"}
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "type" "Type")
              [:br]
              [:div {:class "form-check form-check-inline"}
                (radio-button {:class "form-check-input" :id "credit"} 
                              "type"
                              (if transaction (> (:type transaction) 0) false)
                              1)
                [:span {:class "form-check-label"} "Credit"]]
              [:div {:class "form-check form-check-inline"}
                (radio-button {:class "form-check-input" :id "debit"}
                              "type" 
                              (if transaction (< (:type transaction) 0) false)
                              -1)
                [:span {:class "form-check-label"} "Debit"]]]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "amount" "Amount")
              (text-field {:class "form-control" :id "amount"} 
                          "amount" 
                          (:amount transaction))]]
          [:div {:class "col-md-8"}
            [:div {:class "form-group"}
              (label "description" "Description")
              (text-field {:class "form-control" :id "description"} 
                          "description" 
                          (:description transaction))]]]
               
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))