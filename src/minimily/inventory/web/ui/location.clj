(ns minimily.inventory.web.ui.location
  (:require [hiccup.form                :refer [form-to label submit-button text-field text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn location-page [session location & [goods]]
  (http-headers
    (layout session "Location"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/inventory/locations/delete"]
            (back-button "/inventory/locations")
            (str "&nbsp;")
            (edit-button (str "/inventory/locations/" (:id location) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id location))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-6"} (show-field "Name"        location :name)]
            [:div {:class "col-md-6"} (show-field "Description" location :description)]]]]
      [:br]
      [:h3 "Goods"]
      [:div {:class "card"}
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/inventory/goods/" (:id %))} 
                                       (:name %)]]
                              [:td (:description %)]) goods)]]])))

(defn locations-page [session locations]
  (http-headers 
    (layout session "Locations"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/inventory")
          (str "&nbsp;")
          [:a {:href "/inventory/locations/new" :class "btn btn-secondary"} "New Location"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/inventory/locations/" (:id %))} 
                                       (:name %)]]
                              [:td (:description %)]) locations)]]])))

(defn location-form-page [session & [location]]
  (http-headers
    (layout session "Location"
      (form-to [:post "/inventory/locations/save"]
        (when location (hidden-field "id" (:id location)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name location))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description"
                      (:description location))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/inventory/locations/" (:id location))} "Cancel"]))))