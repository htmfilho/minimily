(ns minimily.accounting.web.ui.transfer-form
  (:require [hiccup.form                :refer [form-to label submit-button
                                                text-field hidden-field 
                                                select-options]]
            [minimily.utils.date        :refer [to-string today]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transfer-form-page [session account to-accounts]
  (http-headers
    (layout session "Transfer"
      (form-to [:post (str "/accounts/" (:id account) "/transfer/perform")]
        [:div {:class "row"}
          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "from" "From Account")
              [:br]
              [:span {:id "from" :class "read-only"} (:name account)]]]
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "balance" "Balance")
              [:br]
              [:span {:id "balance" :class "read-only"} (:balance account)]]]]
        [:div {:class "row"}
          [:div {:class "col-md-8"}
            [:div {:class "form-group"}
              (label "to" "To Account")
              [:select {:name "to" :class "form-control" :id "to"}
                (map #(vector :option {:value (:id %)} (:name %)) to-accounts)]]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "amount" "Amount")
              (text-field {:class "form-control" :id "amount"} "amount")]]
          [:div {:class "col-md-2"}
           [:div {:class "form-group"}
            (label "date_transaction" "Date")
            [:input {:type "date" :id "date_transaction" :name "date_transaction" :class "form-control"
                     :value (to-string (today) "yyyy-MM-dd")}]]]]
        
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))
