(ns minimily.accounting.web.ui.transaction
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field back-button edit-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transaction-page [session account transaction]
  (http-headers
    (layout session "Transaction"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post (str "/accounts/" (:id account) "/transactions/delete")]
            (back-button (str "/accounts/" (:id account)))
            (str "&nbsp;")
            (edit-button (str "/accounts/" (:id account) "/transactions/" (:id transaction) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id transaction))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-3"}
              (show-field "Account" account :name)]
            [:div {:class "col-md-9"}
              (show-field "Balance" transaction :balance)]]
          [:div {:class "row"}
            [:div {:class "col-md-3"}
              [:p
                [:span {:class "label"} "Type"]
                [:br]
                (if (> (:type transaction) 0) "Credit" "Debit")]]
            [:div {:class "col-md-3"}
              (show-field "Amount" transaction :amount)]
            [:div {:class "col-md-6"}
              (show-field "Description" transaction :description)]]]])))
