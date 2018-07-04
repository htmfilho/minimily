(ns minimily.inventory.web.ui.location
  (:require [hiccup.form                :refer [form-to submit-button 
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
