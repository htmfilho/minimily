(ns minimily.documents.web.ui.document-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn document-form-page [session folder & [document]]
  (http-headers
    (layout session "Document"
      [:form {:action (str "/folders/" (:id folder) "/documents/save") 
              :method "POST"
              :enctype "multipart/form-data"}
        (when document (hidden-field "id" (:id document)))
        (show-field "Folder" folder :name)
        [:div {:class "form-group"}
          (label "file" "File")
          [:input {:type "file" :name "file" :id "file" :class "form-control"}]]
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