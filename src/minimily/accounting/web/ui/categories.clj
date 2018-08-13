(ns minimily.accounting.web.ui.categories
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn categories-page [session categories]
  (http-headers
    (layout session "Categories"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/accounting")
          (str "&nbsp;")
          [:a {:href "/accounting/categories/new" :class "btn btn-secondary"} "New Category"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/categories/" (:id %))}
                                       [:i {:class "fas fa-tag"}]
                                       (str "&nbsp;")
                                       (:name %)]]
                              [:td (:description %)]) categories)]]])))
