(ns minimily.accounting.web.ui.third-parties
  (:require [hiccup.form                :refer [form-to]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn third-parties-page [session third-parties]
  (http-headers 
    (layout session "Parties"
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:div {:class "row"}
            [:div {:class "col-md-12"}
             (back-button "/accounting")
             (str "&nbsp;")
             [:a {:href "/accounting/third_parties/new" :class "btn btn-secondary"} "New Party"]]]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/third_parties/" (:id %))} 
                                       (:name %)]]) third-parties)]]])))