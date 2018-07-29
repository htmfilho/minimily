(ns minimily.inventory.web.ui.good
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
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