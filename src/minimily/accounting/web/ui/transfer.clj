(ns minimily.accounting.web.ui.transfer
  (:require [hiccup.form                :refer [form-to label submit-button 
                                                hidden-field text-field]]
            [minimily.utils.date        :refer [to-string today]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field show-field-link show-value back-button edit-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transfers-page [session transfers-from transfers-to]
  (http-headers 
    (layout session "Transfers"
      [:h3 "From"]
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:div {:class "row"}
            [:div {:class "col-md-12"}
             (back-button "/accounting")]]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Description"]
              [:th {:style "text-align: right;"} "Amount"]
              [:th "Created"]
              [:th "Completed"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/transfers/" (:id %))} 
                                       (:description %)]]
                              [:td {:style "text-align: right;"} (:amount %)]
                              [:td (to-string (:date_created %) "MMM dd, yyyy")]
                              [:td (to-string (:date_completed %) "MMM dd, yyyy")]
                              ) transfers-from)
            [:tr
             [:td {:style "text-align: right;"} [:b "Total:"]]
             [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:amount %) transfers-from)))]
             [:td {:colspan "2"}]]]]]
      
      [:br]
      
      [:h3 "To"]
      [:div {:class "card"}
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Description"]
              [:th {:style "text-align: right;"} "Amount"]
              [:th "Created"]
              [:th "Completed"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/transfers/" (:id %))} 
                                       (:description %)]]
                              [:td {:style "text-align: right;"} (:amount %)]
                              [:td (to-string (:date_created %) "MMM dd, yyyy")]
                              [:td (to-string (:date_completed %) "MMM dd, yyyy")]
                              ) transfers-to)
            [:tr
             [:td {:style "text-align: right;"} [:b "Total:"]]
             [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:amount %) transfers-to)))]
             [:td {:colspan "2"}]]]]])))

(defn transfer-page [session transfer accounts]
  (http-headers
    (layout session "Tranfer"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "form_transfer"} [:post (str "/accounting/transfers/" (:id transfer) "/delete")]
            (back-button "/accounting/transfers/")
            (str "&nbsp;")
            (hidden-field "id" (:id transfer)))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-6"}
              (show-field "Description" transfer :description)]
            [:div {:class "col-md-2"}
              (show-field (str "Amount" (if (:currency transfer) 
                                            (str " (" (:currency transfer) ")")
                                            "")) transfer :amount)]
            [:div {:class "col-md-2"}
              [:div {:class "form-group"}
                [:label "Created"]
                [:br]
                (to-string (:date_created transfer) "MMM dd, yyyy")]]
            [:div {:class "col-md-2"} 
              [:div {:class "form-group"}
                [:label "Completed"]
                [:br]
                (to-string (:date_completed transfer) "MMM dd, yyyy")]]]
          (when (and (nil? (:date_completed transfer)) (= (:profile-id session) (:profile_to  transfer)))
            [:div {:class "form-group"}
              (label "account-to" "Account To")
              [:select {:name "account_to" :class "form-control" :id "account-to"}
                       [:option {:value ""} "Select..."]
                       (map #(vector :option {:value (:id %)} (:name %)) accounts)]])]])))

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
              [:div {:class "col-6"}
                [:div {:class "form-group"}
                  (label "to_account" "To Account")
                  [:select {:name "to_account" :class "form-control" :id "to_account"}
                    (conj (map #(vector :option {:value (:id %)} (str (:name %) " (" (:currency %) ")")) to-accounts)
                          [:option {:value ""} "Select..."])]]]
              [:div {:class "col-6"}
                [:div {:class "form-group"}
                  (label "to_user" "To User (Email)")
                  (text-field {:class "form-control" :id "to_user"} "to_user")]]]
            [:div {:class "row"}
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
              [:div {:class "col-md-6"}
                [:div {:class "form-group"}
                  (label "date_transaction" "Date")
                  [:input {:type "date" :id "date_transaction" :name "date_transaction" :class "form-control"
                          :value (to-string (today) "yyyy-MM-dd")}]]]]]]
        
        [:br]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/accounts/" (:id account))} "Cancel"]))))