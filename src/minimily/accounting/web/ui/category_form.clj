(ns minimily.accounting.web.ui.category-form
  (:require [hiccup.form                :refer [form-to label 
                                                submit-button
                                                text-field
                                                text-area
                                                hidden-field
                                                radio-button]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [minimily.accounting.model.transaction :as transaction-model]))

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