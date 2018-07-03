(ns minimily.accounting.web.ui.accounts
  (:require [hiccup.form                :refer [form-to]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn accounts-page [session active-accounts inactive-accounts currencies]
  (http-headers 
    (layout session "Accounts"
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:div {:class "row"}
            [:div {:class "col-md-8"}
             (back-button "/")
             (str "&nbsp;")
             [:a {:href "/accounts/new" :class "btn btn-secondary"} "New Account"]]
            [:div {:class "col-md-4"}
              (form-to [:get "/accounts"]
                [:select {:name "currency" :class "form-control" :id "currency" :onchange "this.form.submit()"}
                  (map #(vector :option {:value (:acronym %)} (str (:acronym %) " (" (:name %) ")")) currencies)])]]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Number"]
              [:th {:style "text-align: right;"} "Balance"]
              [:th "Currency"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounts/" (:id %))} 
                                       (:name %)]]
                              [:td (:number %)]
                              [:td {:style "text-align: right;"} (:balance %)]
                              [:td (:currency %)]) active-accounts)
            [:tr
             [:td {:colspan "2" :style "text-align: right;"} [:b "Total:"]]
             [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:balance %) active-accounts)))]
             [:td]]]]]
      
      [:br]
      [:h3 "Inactive Accounts"]

      [:div {:class "card"}
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Number"]
              [:th {:style "text-align: right;"} "Balance"]
              [:th "Currency"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounts/" (:id %))} 
                                       (:name %)]]
                              [:td (:number %)]
                              [:td {:style "text-align: right;"} (:balance %)]
                              [:td (:currency %)]) inactive-accounts)
            [:tr
             [:td {:colspan "2" :style "text-align: right;"} [:b "Total:"]]
             [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:balance %) inactive-accounts)))]
             [:td]]]]])))