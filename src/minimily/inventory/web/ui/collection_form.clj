(ns minimily.inventory.web.ui.collection-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn collection-form-page [session & [collection]]
  (http-headers
    (layout session "Collection"
      (form-to [:post "/inventory/collections/save"]
        (when collection (hidden-field "id" (:id collection)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name collection))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description"
                      (:description collection))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/inventory/collections/" (:id collection))} "Cancel"]))))