(ns minimily.accounting.web.ui.category-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn category-form-new [session & [parent]]
  (http-headers
    (layout session "Category"
      (form-to [:post "/accounting/categories/save"]
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
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/categories/" (:id parent))} "Cancel"]))))

(defn category-form-edit [session category]
  (http-headers
    (layout session "Category"
      (form-to [:post "/accounting/categories/save"]
        (hidden-field "id" (:id category))
        (when (:parent category) (hidden-field "parent" (:parent category)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name category))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description" 
                      (:description category))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/categories/" (:id category))} "Cancel"]))))