(ns minimily.accounting.web.ui.transaction
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout :refer [layout]]
            [minimily.web.ui.bootstrap :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transaction-page [account transaction]
  (http-headers
    (layout nil "Transaction"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post (str "/accounts/" (:id account) "/transactions/delete")]
            [:a {:href (str "/accounts/" (:id account)) :class "btn btn-secondary"} "Back"]
            (str "&nbsp;")
            [:a {:href (str "/accounts/" (:id account) "/transactions/" (:id transaction) "/edit") 
                :class "btn btn-primary"} "Edit"]
            (str "&nbsp;")
            (hidden-field "id" (:id account))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          (show-field "Account"     account :name)
          (show-field "Description" transaction :description)
          (show-field "Type"        transaction :type)
          (show-field "Amount"      transaction :amount)]])))
