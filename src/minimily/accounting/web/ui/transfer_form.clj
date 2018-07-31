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
      (form-to [:post (str "/accounting/accounts/" (:id account) "/transfer/perform")]
        [:div {:class "card"}
          [:div {:class "card-header"} "From"]
          [:div {:class "card-body"}
            [:div {:class "row"}
              [:div {:class "col-md-4"}
                [:div {:class "form-group"}
                  (label "from" "From Account")
                  [:br]
                  [:span {:id "from" :class "read-only"} (:name account)]]]
              [:div {:class "col-md-2"}
                [:div {:class "form-group"}
                  (label "balance" "Balance")
                  [:br]
                  [:span {:id "balance" :class "read-only"} (:balance account)]]]
              [:div {:class "col-md-2"}
                [:div {:class "form-group"}
                  (label "currency" "Currency")
                  [:br]
                  [:span {:id "currency" :class "read-only"} (:currency account)]]]
              [:div {:class "col-md-4"}
                [:div {:class "form-group"}
                  (label "final_balance" "Final Balance")
                  [:br]
                  [:span {:id "final_balance" :class "read-only"} (:balance account)]]]]
            
            [:div {:class "row justify-content-start"}
              [:div {:class "col-2"}
                [:div {:class "form-group"}
                  (label "amount" (str "Amount From"))
                  (text-field {:class "form-control" :id "amount"} "amount")]]]]]
        
        [:br]
        [:div {:class "card"}
          [:div {:class "card-header"} "To"]
          [:div {:class "card-body"}
            [:div {:class "row"}
              [:div {:class "col-4"}
                [:div {:class "form-group"}
                  (label "to" "To Account")
                  [:select {:name "to" :class "form-control" :id "to"}
                    (conj (map #(vector :option {:value (:id %)} (str (:name %) " (" (:currency %) ")")) to-accounts)
                          [:option {:value ""} "Select..."])]]]
              [:div {:class "col-md-2"}
                [:div {:class "form-group"}
                  [:label {:for "rate" :id "rate_label"} "Rate"]
                  (text-field {:class "form-control" :id "rate" :disabled "disabled"} "rate")]]
              [:div {:class "col-md-2"}
                [:div {:class "form-group"}
                  [:label {:for "amount_to" :id "amount_to_label"} "Amount To"]
                  [:span {:class "form-control" :id "amount_to_view"} "0"]
                  (hidden-field {:id "amount_to"} "amount_to")]]
              [:div {:class "col-md-2"}
                [:div {:class "form-group"}
                  [:label {:for "fee"} (str "Fee (" (:currency account) ")")]
                  (text-field {:class "form-control" :id "fee" :disabled "disabled"} "fee")]]
              [:div {:class "col-md-2"}
                [:div {:class "form-group"}
                  (label "date_transaction" "Date")
                  [:input {:type "date" :id "date_transaction" :name "date_transaction" :class "form-control"
                          :value (to-string (today) "yyyy-MM-dd")}]]]]]]
        
        [:br]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/accounts/" (:id account))} "Cancel"]))))
