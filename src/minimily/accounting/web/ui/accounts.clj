(ns minimily.accounting.web.ui.accounts
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn accounts-page [session accounts]
  (http-headers 
    (layout session "Accounts"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/")
          (str "&nbsp;")
          [:a {:href "/accounts/new" :class "btn btn-secondary"} "New Account"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Number"]
              [:th "Balance"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounts/" (:id %))} 
                                       (:name %)]]
                              [:td (:number %)]
                              [:td {:style "text-align: right;"} (:balance %)]) accounts)
            [:td {:colspan "2" :style "text-align: right;"} [:b "Total:"]]
            [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:balance %) accounts)))]]]])))
