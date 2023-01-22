(ns minimily.accounting.web.ui.third-party
  (:require [hiccup.form                :refer [form-to submit-button label text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn third-party-page [session third-party accounts]
  (http-headers
    (layout session "Third Party"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/accounting/third_parties/delete"]
            (back-button "/accounting/third_parties")
            (str "&nbsp;")
            (edit-button (str "/accounting/third_parties/" (:id third-party) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id third-party))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          (show-field "Name" third-party :name)]]
      [:br]
          
      (when (not (empty? accounts))
        [:div
          [:ul {:class "nav nav-tabs" :id "third-party-tabs" :role "tablist"}
            [:li {:class "nav-item"}
              [:a {:class "nav-link active" :id "accounts-tab" :data-toggle "tab"
                  :href "#accounts-panel" :role "tab" :aria-controls "accounts-panel"
                  :aria-selected "true"} "Accounts"]]]

          [:div {:class "tab-content" :id "accounts-tab-content"}
            [:div {:class "tab-pane fade show active" :id "accounts-panel" :role "tabpanel" :aria-labelledby "accounts-tab"}
              [:div {:class "card"}
                [:table {:class "table table-striped"}
                  [:thead
                    [:tr
                      [:th "Name"]
                      [:th {:style "text-align: right;"} "Balance"]
                      [:th "Currency"]]]
                  [:tbody
                    (map #(vector :tr [:td [:a {:href (str "/accounting/accounts/" (:id %))} (:name %)]]
                                      [:td {:style "text-align: right;"} (:balance %)]
                                      [:td (:currency %)]) accounts)]]]]]]))))

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

(defn third-party-form-page [session  & [third-party]]
  (http-headers
    (layout session "Party"
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