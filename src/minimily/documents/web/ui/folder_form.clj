(ns minimily.documents.web.ui.folder-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn folder-form-new [session & [parent]]
  (http-headers
    (layout session "Folder"
      (form-to [:post "/folders/save"]
        (when parent (hidden-field "parent" (:id parent)))
        (when parent (show-field "Parent" parent :name))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name")]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description")]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/folders/" (:id parent))} "Cancel"]))))

(defn folder-form-edit [session & [folder]]
  (http-headers
    (layout session "Folder"
      (form-to [:post "/folders/save"]
        (when folder (hidden-field "id" (:id folder)))
        (when (:parent folder) (hidden-field "parent" (:parent folder)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name folder))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description" 
                      (:description folder))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/folders/" (:id folder))} "Cancel"]))))