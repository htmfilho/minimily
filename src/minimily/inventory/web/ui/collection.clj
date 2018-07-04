(ns minimily.inventory.web.ui.collection
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn collection-page [session collection & [goods]]
  (http-headers
    (layout session "Collection"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/inventory/collections/delete"]
            (back-button "/inventory/collections")
            (str "&nbsp;")
            (edit-button (str "/inventory/collections/" (:id collection) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id collection))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-6"} (show-field "Name"        collection :name)]
            [:div {:class "col-md-6"} (show-field "Description" collection :description)]]]]
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
