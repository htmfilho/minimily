(ns minimily.inventory.web.ui.locations
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn locations-page [session locations]
  (http-headers 
    (layout session "Locations"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/")
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
