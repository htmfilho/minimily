(ns minimily.documents.web.ui.document-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer :all]
            [minimily.utils.web.wrapper :refer [http-headers]]))

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