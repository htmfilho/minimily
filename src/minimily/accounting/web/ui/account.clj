(ns minimily.accounting.web.ui.account
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout :refer [layout]]
            [minimily.web.ui.bootstrap :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-page [account]
  (http-headers
    (layout nil "Account"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to [:post "/accounts/delete"]
            [:a {:href "/accounts" :class "btn btn-secondary"} "Back"]
            (str "&nbsp;")
            (hidden-field "id" (:id account))
            (submit-button {:class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          (show-field "Name"   account :name)
          (show-field "Number" account :number)]])))
