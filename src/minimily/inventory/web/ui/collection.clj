(ns minimily.inventory.web.ui.collection
  (:require [hiccup.form                :refer [form-to submit-button label text-field
                                                text-area hidden-field]]
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

(defn collections-page [session collections]
  (http-headers 
    (layout session "Collections"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/inventory")
          (str "&nbsp;")
          [:a {:href "/inventory/collections/new" :class "btn btn-secondary"} "New Collection"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/inventory/collections/" (:id %))} 
                                       (:name %)]]
                              [:td (:description %)]) collections)]]])))

(defn collection-form-page [session & [collection]]
  (http-headers
    (layout session "Collection"
      (form-to [:post "/inventory/collections/save"]
        (when collection (hidden-field "id" (:id collection)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name collection))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description"
                      (:description collection))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/inventory/collections/" (:id collection))} "Cancel"]))))