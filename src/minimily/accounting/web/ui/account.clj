(ns minimily.accounting.web.ui.account
  (:require [minimily.web.ui.layout :refer [layout]]
            [minimily.web.ui.bootstrap :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-page [account]
  (http-headers
    (layout nil "Account"
      [:div {:class "card"}
        [:div {:class "card-header"}
         [:a {:href "/accounts" :class "btn btn-secondary"} "Back"]]
        [:div {:class "card-body"}
          (show-field "Name"   account :name)
          (show-field "Number" account :number)]])))
