(ns minimily.accounting.web.ui.third-party-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :as bootstrap]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn third-party-form-page [session  & [third-party]]
  (http-headers
    (layout session "Third Party"
      (form-to [:post "/accounting/third_parties/save"]
        (when third-party (hidden-field "id" (:id third-party)))
            [:div {:class "form-group"}
              (label "name" "Name")
              (text-field {:class "form-control" :id "name" :maxlength "30"} 
                "name" 
                      (when third-party (:name third-party)))]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/third_parties/"
                                                           (when third-party (:id third-party)))} "Cancel"]))))