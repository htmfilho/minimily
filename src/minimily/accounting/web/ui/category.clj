(ns minimily.accounting.web.ui.category
  (:require [hiccup.form                           :refer [form-to label submit-button 
                                                           hidden-field text-field text-area radio-button]]
            [minimily.web.ui.layout                :refer [layout]]
            [minimily.web.ui.bootstrap             :refer [back-button edit-button show-field]]
            [minimily.utils.date                   :refer [to-string]]
            [minimily.utils.web.wrapper            :refer [http-headers]]
            [minimily.accounting.model.transaction :as transaction-model]))

(defn categories-page [session categories]
  (http-headers
    (layout session "Categories"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (back-button "/accounting")
          (str "&nbsp;")
          [:a {:href "/accounting/categories/new" :class "btn btn-secondary"} "New Category"]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Name"]
              [:th "Type"]
              [:th "Description"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/categories/" (:id %))}
                                       [:i {:class "fas fa-tag"}]
                                       (str "&nbsp;")
                                       (:name %)]]
                              [:td (if (> (:transaction_type %) 0) "Credit" "Debit")]
                              [:td (:description %)]) categories)]]])))

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

(defn category-form-new [session & [parent]]
  (http-headers
    (layout session "Category"
      (form-to [:post "/accounting/categories/save"]
        (when parent (hidden-field "parent" (:id parent)))
        (when parent (show-field "Parent" parent :name))
        
        [:div {:class "row"}
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "name" "Name")
              (text-field {:class "form-control" :id "name"} 
                          "name")]]

          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "type" "Type")
              [:br]
              [:div {:class "form-check form-check-inline"}
                (radio-button {:class "form-check-input" :id "credit"}
                              "transaction_type"
                              false
                              transaction-model/CREDIT)
                [:span {:class "form-check-label"} "Credit"]]
              [:div {:class "form-check form-check-inline"}
                (radio-button {:class "form-check-input" :id "debit"}
                              "transaction_type"
                              false
                              transaction-model/DEBIT)
                [:span {:class "form-check-label"} "Debit"]]]]]

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
        
        [:div {:class "row"}
          [:div {:class "col-md-9"}
            [:div {:class "form-group"}
              (label "name" "Name")
              (text-field {:class "form-control" :id "name"} 
                          "name" 
                          (:name category))]]

          [:div {:class "col-md-3"}
            [:div {:class "form-group"}
              (label "type" "Type")
              [:br]
              [:div {:class "form-check form-check-inline"}
                (radio-button {:class "form-check-input" :id "credit"}
                              "transaction_type"
                              (= (:transaction_type category) transaction-model/CREDIT)
                              transaction-model/CREDIT)
                [:span {:class "form-check-label"} "Credit"]]
              [:div {:class "form-check form-check-inline"}
                (radio-button {:class "form-check-input" :id "debit"}
                              "transaction_type"
                              (= (:transaction_type category) transaction-model/DEBIT)
                              transaction-model/DEBIT)
                [:span {:class "form-check-label"} "Debit"]]]]]
        
        [:div {:class "form-group"}
          (label "description" "Description")
          (text-area {:class "form-control" :id "description"} 
                      "description" 
                      (:description category))]
        (submit-button {:class "btn btn-primary"} "Submit")

        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/categories/" (:id category))} "Cancel"]))))