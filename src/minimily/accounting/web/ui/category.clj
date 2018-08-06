(ns minimily.accounting.web.ui.category
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button edit-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn category-page [session category children path]
  (http-headers
    (layout session 
      [:span [:i {:class "fas fa-tag"}] (str "&nbsp;") (:name category)]
      [:p (:description category)]
      [:nav {:aria-label "breadcrumb"}
        [:ol {:class "breadcrumb"}
          [:li {:class "breadcrumb-item"} [:a {:href "/accounting/categories"} [:i {:class "fas fa-university"}]]]
          (map #(vector :li 
                        {:class "breadcrumb-item"} 
                        (if (= % (last path))
                          (:name %)
                          [:a {:href (str "/accounting/categories/" (:id %))} (:name %)])) path)]]
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/accounting/categories/delete"]
            (back-button (str "/accounting/categories/" (:parent category)))
            (str "&nbsp;")
            [:a {:href (str "/accounting/categories/new?parent=" (:id category)) :class "btn btn-secondary"} "New Category"]
            (str "&nbsp;")
            (edit-button (str "/accounting/categories/" (:id category) "/edit"))
            (hidden-field "id" (:id category))
            (str "&nbsp;")
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td 
                                  [:span 
                                    [:i {:class "fas fa-tag"}]
                                    (str "&nbsp;")
                                    [:a {:href (str "/accounting/categories/" (:id %))} (:name %)]]]
                              [:td (:description %)]) children)]]])))
