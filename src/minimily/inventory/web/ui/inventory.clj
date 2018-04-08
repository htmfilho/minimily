(ns minimily.inventory.web.ui.inventory
  (:require [minimily.web.ui.layout :refer [layout]]
  [minimily.utils.web.wrapper       :refer [http-headers]]))

(defn inventory-page [session]
  (http-headers 
    (layout session (:full-name session) 
      [:div {:class "list-group"}
        [:a {:href "/locations"
             :class "list-group-item list-group-item-action flex-column align-items-start"}
          [:div {:class "d-flex w-100 justify-content-between"}
            [:h3 {:class "mb-1"} "Locations"]]
          [:p {:class "mb-1"} "The locations where you organize your goods."]]
        [:a {:href "/collections"
             :class "list-group-item list-group-item-action flex-column align-items-start"}
          [:div {:class "d-flex w-100 justify-content-between"}
            [:h3 {:class "mb-1"} "Collections"]]
          [:p {:class "mb-1"} "The collections you classify your goods."]]])))