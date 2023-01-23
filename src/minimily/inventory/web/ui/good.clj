(ns minimily.inventory.web.ui.good
  (:require [hiccup.form                :refer [form-to label submit-button text-field
                                                text-area hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field show-field-link back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn good-page [session good]
  (http-headers
    (layout session "Good"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/inventory/goods/delete"]
            (back-button (str "/inventory/goods?location=" (:location_id good) "&collection=" (:collection_id good)))
            (str "&nbsp;")
            (edit-button (str "/inventory/goods/" (:id good) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id good))
            (hidden-field "location" (:location_id good))
            (hidden-field "collection" (:collection_id good))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-6"} (show-field "Name"     good :name)]
            [:div {:class "col-md-3"} (show-field "Quantity" good :quantity)]
            [:div {:class "col-md-3"} (show-field "Value"    good :value)]]
          (show-field "Description" good :description)
          [:div {:class "row"}
            [:div {:class "col-md-6"} (show-field-link "Location" good :location (str "/inventory/locations/" (:location_id good)))]
            [:div {:class "col-md-6"} (show-field-link "Collection" good :collection (str "/inventory/collections/" (:collection_id good)))]]]])))

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

(defn good-form-page [session locations collections & [good]]
  (http-headers
    (layout session "Good"
      (form-to [:post "/inventory/goods/save"]
        (when good (hidden-field "id" (:id good)))
        [:div {:class "row"}
          [:div {:class "col-md-6"}
            [:div {:class "form-group"}
              (label "name" "Name")
              (text-field {:class "form-control" :id "name"} 
                          "name"
                          (:name good))]]
          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "quantity" "Quantity")
              (text-field {:class "form-control" :id "quantity"} 
                          "quantity"
                          (:quantity good))]]
          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "value" "Value")
              (text-field {:class "form-control" :id "value"} 
                          "value"
                          (:value good))]]]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description"
                      (:description good))]
        [:div {:class "row"}
          [:div {:class "col-md-6"}
            [:div {:class "form-group"}
              (label "location" "Locations")
              [:select {:name "location" :class "form-control" :id "location"}
                (map #(vector :option (if (:selected %) 
                                      {:value (:id %) :selected "true"}
                                      {:value (:id %)}) (:name %)) locations)]]]
          [:div {:class "col-md-6"}
            [:div {:class "form-group"}
              (label "collection" "Collections")
              [:select {:name "collection" :class "form-control" :id "collection"}
                (map #(vector :option (if (:selected %) 
                                        {:value (:id %) :selected "true"}
                                        {:value (:id %)}) (:name %)) collections)]]]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/inventory/goods/" (:id good))} "Cancel"]))))