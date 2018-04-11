(ns minimily.inventory.web.ui.collections
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

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
