(ns minimily.inventory.web.ui.location-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn location-form-page [session & [location]]
  (http-headers
    (layout session "Location"
      (form-to [:post "/inventory/locations/save"]
        (when location (hidden-field "id" (:id location)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name"} 
                      "name" 
                      (:name location))]
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description"
                      (:description location))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/inventory/locations/" (:id location))} "Cancel"]))))