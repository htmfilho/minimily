(ns minimily.accounting.web.ui.accounts
  (:require [hiccup.form                :refer [form-to]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn accounts-page [session active-accounts inactive-accounts currencies]
  (http-headers 
    (layout session "Accounts"
      [:ul {:class "nav nav-tabs" :id "accounts-tabs" :role "tablist"}
        [:li {:class "nav-item"}
          [:a {:class "nav-link active" :id "active-tab" :data-toggle "tab"
               :href "#active-panel" :role "tab" :aria-controls "active-panel"
               :aria-selected "true"} "Active"]]
        [:li {:class "nav-item" }
          [:a {:class "nav-link" :id "inactive-tab" :data-toggle "tab"
               :href "#inactive-panel" :role "tab" :aria-controls "inactive-panel"
               :aria-selected "false"} "Inactive"]]
        [:li {:class "nav-item" }
          [:a {:class "nav-link" :id "third-party-tab" :data-toggle "tab"
               :href "#third-parties-panel" :role "tab" :aria-controls "third-parties-panel"
               :aria-selected "false"} "Third Parties"]]]

      [:div {:class "tab-content" :id "tabs-content"}
        [:br]
        [:div {:class "tab-pane fade show active" :id "active-panel" :role "tabpanel"
               :aria-labelledby "active-tab"}
          [:div {:class "card"}
            [:div {:class "card-header"}
                (back-button "/accounting")
                (str "&nbsp;")
                [:a {:href "/accounting/accounts/new" :class "btn btn-secondary"} "New Account"]]
            [:table {:class "table table-striped"}
              [:thead
                [:tr 
                  [:th "Name"]
                  [:th {:style "text-align: right;"} "Balance"]
                  [:th "Currency"]]]
              [:tbody 
                (map #(vector :tr [:td [:a {:href (str "/accounting/accounts/" (:id %))} 
                                          (:name %)]]
                                  [:td {:style "text-align: right;"} (:balance %)]
                                  [:td (:currency %)]) active-accounts)
                [:tr
                [:td {:style "text-align: right;"} [:b "Total:"]]
                [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:balance %) active-accounts)))]
                [:td]]]]]]

        [:div {:class "tab-pane fade show" :id "inactive-panel" :role "tabpanel"
               :aria-labelledby "inactive-tab"}
          [:div {:class "card"}
            [:table {:class "table table-striped"}
              [:thead
                [:tr 
                  [:th "Name"]
                  [:th {:style "text-align: right;"} "Balance"]
                  [:th "Currency"]]]
              [:tbody 
                (map #(vector :tr [:td [:a {:href (str "/accounting/accounts/" (:id %))} 
                                          (:name %)]]
                                  [:td {:style "text-align: right;"} (:balance %)]
                                  [:td (:currency %)]) inactive-accounts)
                [:tr
                [:td {:style "text-align: right;"} [:b "Total:"]]
                [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:balance %) inactive-accounts)))]
                [:td]]]]]]
                
        [:div {:class "tab-pane fade show" :id "third-parties-panel" :role "tabpanel"
               :aria-labelledby "third-parties-tab"}]])))