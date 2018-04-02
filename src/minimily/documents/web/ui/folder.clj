(ns minimily.documents.web.ui.folder
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button edit-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn folder-page [session folder children path]
  (http-headers
    (layout session (if (empty? children)
                        [:span [:i {:class "far fa-folder-open"}] (str "&nbsp;") (:name folder)]
                        [:span [:i {:class "fas fa-folder-open"}] (str "&nbsp;") (:name folder)])
      [:p (:description folder)]
      [:nav {:aria-label "breadcrumb"}
        [:ol {:class "breadcrumb"}
          [:li {:class "breadcrumb-item"} [:a {:href "/folders"} [:i {:class "fas fa-university"}]]]
          (map #(vector :li {:class "breadcrumb-item"} [:a {:href (str "/folders/" (:id %))} (:name %)]) path)
        ]]
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/folders/delete"]
            (back-button (str "/folders/" (:parent folder)))
            (str "&nbsp;")
            [:a {:href (str "/folders/new?parent=" (:id folder)) :class "btn btn-secondary"} "New Folder"]
            (str "&nbsp;")
            (edit-button (str "/folders/" (:id folder) "/edit"))
            (hidden-field "id" (:id folder))
            (str "&nbsp;")
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td 
                                [:a {:href (str "/folders/" (:id %))} 
                                  (if (> (:num-children %) 0) 
                                    [:i {:class "fas fa-folder"}]
                                    [:i {:class "far fa-folder"}])
                                  (str "&nbsp;")
                                  (:name %)]]
                              [:td (:description %)]) children)]]])))
