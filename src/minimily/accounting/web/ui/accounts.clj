(ns minimily.accounting.web.ui.accounts
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn accounts-page [session accounts]
  (http-headers 
    (layout session "Accounts"
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:a {:href "/accounts/new" :class "btn btn-secondary"} "New Account"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Number"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounts/" (:id %))} (:name %)]]
                              [:td (:number %)]) accounts)]]])))
