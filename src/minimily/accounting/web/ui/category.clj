(ns minimily.accounting.web.ui.category
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn category-page [session category children path transactions]
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
        (when (not (empty? children))
          [:table {:class "table table-striped"}
            [:thead
              [:tr 
                [:th "Name"]
                [:th "Type"]
                [:th "Description"]]]
            [:tbody 
              (map #(vector :tr [:td 
                                    [:span 
                                      [:i {:class "fas fa-tag"}]
                                      (str "&nbsp;")
                                      [:a {:href (str "/accounting/categories/" (:id %))} (:name %)]]]
                                [:td (if (> (:transaction_type %) 0) "Credit" "Debit")]
                                [:td (:description %)]) children)]])]
      [:br]

      (when (not (empty? transactions))
        [:div
          [:ul {:class "nav nav-tabs" :id "category-tabs" :role "tablist"}
            [:li {:class "nav-item"}
              [:a {:class "nav-link active" :id "transactions-tab" :data-toggle "tab"
                  :href "#transactions-panel" :role "tab" :aria-controls "transactions-panel"
                  :aria-selected "true"} "Transactions"]]]

          [:div {:class "tab-content" :id "myTabContent"}
            [:div {:class "tab-pane fade show active" :id "transactions-panel" :role "tabpanel" :aria-labelledby "transactions-tab"}
              [:div {:class "card"}
                [:table {:class "table table-striped"}
                  [:thead
                    [:tr
                      [:th "Description"]
                      [:th "Type"]
                      [:th {:style "text-align: right;"} "Amount"]
                      [:th "Date"]
                      [:th ""]]]
                  [:tbody
                    (map #(vector :tr [:td [:a {:href (str "/accounting/accounts/" (:account %) "/transactions/" (:id %))}
                                              (:description %)]]
                                      [:td (if (> (:type %) 0) "Credit" "Debit")]
                                      [:td {:style "text-align: right;"}
                                            (if (< (:type %) 0)
                                              [:span {:class "debit"} (:amount %)]
                                              [:span {:class "credit"} (:amount %)])]
                                      [:td (to-string (:date_transaction %) "MMM dd, yyyy")]
                                      [:td (when (:account_transfer %) [:a {:href (str "/accounting/accounts/" (:account_transfer %))} [:i {:class "fas fa-link"}]])]) transactions)]]]]]]))))
