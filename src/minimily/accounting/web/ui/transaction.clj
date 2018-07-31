(ns minimily.accounting.web.ui.transaction
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field show-value back-button edit-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transaction-page [session account transaction]
  (http-headers
    (layout session "Transaction"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post (str "/accounting/accounts/" (:id account) "/transactions/delete")]
            (back-button (str "/accounting/accounts/" (:id account)))
            (str "&nbsp;")
            (edit-button (str "/accounting/accounts/" (:id account) "/transactions/" (:id transaction) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id transaction))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-3"}
              (show-field "Account" account :name)]
            [:div {:class "col-md-9"}
              (show-field (str "Balance (" (:currency account) ")") account :balance)]]
          [:div {:class "row"}
            [:div {:class "col-md-1"}
              (show-field "Type" transaction :type "Credit" "Debit")]
            [:div {:class "col-md-2"}
              (show-field (str "Amount (" (:currency account) ")") transaction :amount)]
            [:div {:class "col-md-7"}
              (show-field "Description" transaction :description)]
            [:div {:class "col-md-2"}
              (show-value "Date" (to-string (:date_transaction transaction) "MMM dd, yyyy"))]]]])))
