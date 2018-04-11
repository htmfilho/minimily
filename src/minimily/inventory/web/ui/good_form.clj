(ns minimily.inventory.web.ui.good-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

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