(ns minimily.documents.web.ui.document
  (:require [hiccup.form                :refer [form-to submit-button label text-field text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer :all]
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
            [:div {:class "col-md-4"} (show-value "Size" (format "%.1f MB" (/ (:file_size document) 1000000.0)))]]]]
      [:br]     
      [:div {:class "card"}
        [:div {:class "card-body"}
          [:img {:src (str "/folders/" (:id folder) "/documents/" (:id document) "/file") :class "img-fluid"}]]])))

(defn document-form-add [session folder]
  (http-headers
    (layout session "Document"
      [:form {:action (str "/folders/" (:id folder) "/documents/save") 
              :method "POST"
              :enctype "multipart/form-data"}
        (show-field "Folder" folder :name)
        [:div {:class "form-group"}
          (label "file" "File")
          [:input {:type "file" :name "file" :id "file" :class "form-control"}]]
        [:div {:class "form-group"}
          (label "title" "Title")
          (text-field {:class "form-control" :id "title"} 
                      "title")]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                     "description")]

        (submit-button {:class "btn btn-primary"
                        :formaction (str "/folders/" (:id folder) "/documents/save")} "Submit")
        (str "&nbsp;")
        
        (submit-button {:class "btn btn-primary"
                        :formaction (str "/folders/" (:id folder) "/documents/saveandnew")} "Submit and New")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" 
             :href (str "/folders/" (:id folder))} "Cancel"]])))

(defn document-form-edit [session folder document]
  (http-headers
    (layout session "Document"
      [:form {:action (str "/folders/" (:id folder) "/documents/save") 
              :method "POST"
              :enctype "multipart/form-data"}
        (hidden-field "id" (:id document))
        (show-field "Folder" folder :name)
        [:div {:class "form-group"}
          (show-field-link "File" document :file_original_name 
                           (str "/folders/" (:id folder) "/documents/" (:id document) "/file")
                           (:file_original_name document))]
        [:div {:class "form-group"}
          (label "title" "Title")
          (text-field {:class "form-control" :id "title"} 
                      "title" 
                      (:title document))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                     "description" 
                     (:description document))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" 
             :href (str "/folders/" (:id folder))} "Cancel"]])))