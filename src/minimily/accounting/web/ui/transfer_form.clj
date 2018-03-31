(ns minimily.accounting.web.ui.transfer-form
  (:require [hiccup.form                :refer [form-to label submit-button
                                                text-field hidden-field 
                                                select-options]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transfer-form-page [session account to-accounts]
  (println session)
  (println account)
  (println to-accounts)
  (http-headers
    (layout session "Transfer"
      (form-to [:post (str "/accounts/" (:id account) "/transfer/perform")]
        [:div {:class "row"}
          [:div {:class "col-md-4"}
            [:div {:class "form-group"}
              (label "from" "From Account")
              [:br]
              [:span {:id "from" :class "read-only"} (:name account)]]]
          [:div {:class "col-md-6"}
            [:div {:class "form-group"}
              (label "to" "To Account")
              [:select {:name "to" :class "form-control"}
                (map #(vector :option {:value (:id %)} (:name %)) 
                     to-accounts)]]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "amount" "Amount")
              (text-field {:class "form-control" :id "amount"} 
                          "amount")]]]        
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounts/" (:id account))} "Cancel"]))))
