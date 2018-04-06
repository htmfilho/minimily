(ns minimily.documents.web.ui.document
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button edit-button 
                                                show-field show-field-link
                                                show-value]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn document-page [session document folder path]
  (http-headers
    (layout session [:span [:i {:class "far fa-file-alt"}] (str "&nbsp;") (:title document)]
      [:nav {:aria-label "breadcrumb"}
        [:ol {:class "breadcrumb"}
          [:li {:class "breadcrumb-item"} [:a {:href "/folders"} [:i {:class "fas fa-university"}]]]
          (map #(vector :li 
                        {:class "breadcrumb-item"} 
                        (if (= % (last path))
                          (:name %)
                          [:a {:href (str "/folders/" (:id %))} (:name %)])) path)]]
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post (str "/folders/" (:id folder) "/documents/delete")]
            (back-button (str "/folders/" (:id folder)))
            (str "&nbsp;")
            (edit-button (str "/folders/" (:id folder) "/documents/" (:id document) "/edit"))
            (hidden-field "id" (:id document))
            (str "&nbsp;")
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-4"} (show-field "Folder" folder :name)]
            [:div {:class "col-md-4"} (show-field "Title" document :title)]
            [:div {:class "col-md-4"} (show-field "Description" document :description)]]
          [:div {:class "row"}
            [:div {:class "col-md-4"} (show-field-link "File" document :file_original_name 
                                                       (str "/folders/" (:id folder) "/documents/" (:id document) "/file")
                                                       (:file_original_name document))]
            [:div {:class "col-md-4"} (show-field "Format" document :file_format)]
            [:div {:class "col-md-4"} (show-value "Size" (format "%.1f MB" (/ (:file_size document) 1000000.0)))]]
          
          
          ]])))
