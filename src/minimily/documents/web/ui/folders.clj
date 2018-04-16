(ns minimily.documents.web.ui.folders
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn folders-page [session folders]
  (http-headers 
    (layout session "Documents"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/")
          (str "&nbsp;")
          [:a {:href "/folders/new" :class "btn btn-secondary"} "New Folder"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/folders/" (:id %))}
                                       (if (> (:num-children %) 0) 
                                         [:i {:class "fas fa-folder"}]
                                         [:i {:class "far fa-folder"}])
                                       (str "&nbsp;")
                                       (:name %)]]
                              [:td (:description %)]) folders)]]])))
