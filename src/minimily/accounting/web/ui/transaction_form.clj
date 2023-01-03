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

(defn transaction-form-add [session account third-parties]
  (http-headers
    (layout session "Transaction"
      (form-to [:post (str "/accounting/accounts/" (:id account) "/transactions/add")]
        [:div {:class "row"}
          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "acc" "Account")
              [:br]
              [:span {:id "acc" :class "read-only"} (:name account)]]]
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "balance" (str "Balance (" (:currency account) ")"))
              [:br]
              [:span {:id "balance" :class "read-only"} (:balance account)]]]]
        
        [:div {:class "row"}
          [:div {:class "col-md-3"}
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
                [:span {:class "form-check-label"} "Debit"]]]]
          [:div {:class "col-md-5"}
            [:div {:class "form-group"}
              (label "third-party" "Third Party")
              [:select {:name "third_party" :class "form-control" :id "third-party"}
                       [:option {:value ""} "Select..."]
                       (map #(vector :option (if (:selected %)
                                               {:value (:id %) :selected "true"}
                                               {:value (:id %)}) (:name %)) third-parties)]]]
          [:div {:class "col-md-4"}
            [:div {:class "form-group"}
              (label "third-party-account" "Third Party Account")
              [:select {:name "third_party_account" :class "form-control" :id "third-party-account"}
                       [:option {:value ""} "Select..."]]]]]
        
        [:div {:class "row"}
          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "amount" (str "Amount (" (:currency account) ")"))
              (text-field {:class "form-control" :id "amount"} 
                          "amount")]]
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "category" "Category")
              [:select {:name "category" :class "form-control" :id "category"}
                       [:option {:value ""} "Select..."]]]]]

        [:div {:class "row"}
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "description" "Description")
              (text-field {:class "form-control" :id "description"} 
                          "description")]]
          [:div {:class "col-md-3"}
           [:div {:class "form-group"}
            (label "date_transaction" "Date")
            [:input {:type "date" :id "date_transaction" :name "date_transaction" :class "form-control"
                     :value (to-string (today) "yyyy-MM-dd")}]]]]
               
        (submit-button {:class "btn btn-primary"
                        :formaction (str "/accounting/accounts/" (:id account) "/transactions/add")} "Save")
        (str "&nbsp;")
        (submit-button {:class "btn btn-primary"
                        :formaction (str "/accounting/accounts/" (:id account) "/transactions/addandnew")} "Save and New")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/accounts/" (:id account))} "Cancel"]))))

(defn transaction-form-edit [session account transaction categories]
  (http-headers
    (layout session "Transaction"
      (form-to [:post (str "/accounting/accounts/" (:id account) "/transactions/save")]
        (hidden-field "id" (:id transaction))
        [:div {:class "row"}
          [:div {:class "col-md-3"}
            (show-field "Account" account :name)]
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "category" "Category")
              [:select {:name "category" :class "form-control" :id "category"}
                       (conj (map #(vector :option (if (:selected %)
                                                     {:value (:id %) :selected "true"}
                                                     {:value (:id %)}) (:name %)) categories)
                             [:option {:value ""} "Select..."])]]]]
        [:div {:class "row"}
          [:div {:class "col-md-1"}
            [:div {:class "form-group"}
              [:p 
                [:span {:class "label"} "Type"]
                [:br]
                (if (> (:type transaction) 0)
                  "Credit"
                  "Debit")]]]
          [:div {:class "col-md-2"}
            (show-field (str "Amount" (if (:currency account) 
                                        (str " (" (:currency account) ")")
                                        "")) transaction :amount)]
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
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/accounts/" (:id account))} "Cancel"]))))