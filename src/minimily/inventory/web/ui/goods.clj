(ns minimily.inventory.web.ui.goods
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn goods-page [session goods locations collections]
  (http-headers 
    (layout session "Goods"
      [:p
        (back-button "/inventory")
        (str "&nbsp;")
        [:a {:href "/inventory/goods/new" :class "btn btn-secondary"} "New Good"]]
      
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:form {:class "form-inline"}
            [:select {:name "location" :class "form-control mr-sm-2" :id "location"}
              [:option {:value ""} "Locations"]
              (map #(vector :option (if (:selected %) 
                                      {:value (:id %) :selected "true"}
                                      {:value (:id %)}) (:name %)) locations)]
            [:select {:name "collection" :class "form-control  my-2 my-sm-0" :id "location"}
              [:option {:value ""} "Collections"]
              (map #(vector :option (if (:selected %) 
                                      {:value (:id %) :selected "true"}
                                      {:value (:id %)}) (:name %)) collections)]
            (str "&nbsp;")
            [:input {:type "submit" :id "bt_search" :class "btn btn-primary" :value "Search"}]]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/inventory/goods/" (:id %))} 
                                       (:name %)]]
                              [:td (:description %)]) goods)]]])))
