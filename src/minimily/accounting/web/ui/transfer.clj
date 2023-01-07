(ns minimily.accounting.web.ui.transfer
  (:require [hiccup.form                :refer [form-to label submit-button 
                                                hidden-field]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field show-field-link show-value back-button edit-button]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn transfers-page [session transfers-from transfers-to]
  (http-headers 
    (layout session "Transfers"
      [:h3 "From"]
      [:div {:class "card"}
        [:div {:class "card-header"}
          [:div {:class "row"}
            [:div {:class "col-md-12"}
             (back-button "/accounting")]]]
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Description"]
              [:th {:style "text-align: right;"} "Amount"]
              [:th "Created"]
              [:th "Completed"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/transfers/" (:id %))} 
                                       (:description %)]]
                              [:td {:style "text-align: right;"} (:amount %)]
                              [:td (to-string (:date_created %) "MMM dd, yyyy")]
                              [:td (to-string (:date_completed %) "MMM dd, yyyy")]
                              ) transfers-from)
            [:tr
             [:td {:style "text-align: right;"} [:b "Total:"]]
             [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:amount %) transfers-from)))]
             [:td {:colspan "2"}]]]]]
      
      [:br]
      
      [:h3 "To"]
      [:div {:class "card"}
        [:table {:class "table table-striped"}
          [:thead
            [:tr 
              [:th "Description"]
              [:th {:style "text-align: right;"} "Amount"]
              [:th "Created"]
              [:th "Completed"]]]
          [:tbody 
            (map #(vector :tr [:td [:a {:href (str "/accounting/transfers/" (:id %))} 
                                       (:description %)]]
                              [:td {:style "text-align: right;"} (:amount %)]
                              [:td (to-string (:date_created %) "MMM dd, yyyy")]
                              [:td (to-string (:date_completed %) "MMM dd, yyyy")]
                              ) transfers-to)
            [:tr
             [:td {:style "text-align: right;"} [:b "Total:"]]
             [:td {:style "text-align: right;"} (reduce + (filter #(not (nil? %)) (map #(:amount %) transfers-to)))]
             [:td {:colspan "2"}]]]]])))

(defn transfer-page [session transfer accounts]
  (http-headers
    (layout session "Tranfer"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "form_transfer"} [:post (str "/accounting/transfers/" (:id transfer) "/delete")]
            (back-button "/accounting/transfers/")
            (str "&nbsp;")
            (hidden-field "id" (:id transfer)))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-6"}
              (show-field "Description" transfer :description)]
            [:div {:class "col-md-2"}
              (show-field (str "Amount" (if (:currency transfer) 
                                            (str " (" (:currency transfer) ")")
                                            "")) transfer :amount)]
            [:div {:class "col-md-2"}
              [:div {:class "form-group"}
                [:label "Created"]
                [:br]
                (to-string (:date_created transfer) "MMM dd, yyyy")]]
            [:div {:class "col-md-2"} 
              [:div {:class "form-group"}
                [:label "Completed"]
                [:br]
                (to-string (:date_completed transfer) "MMM dd, yyyy")]]]
          (when (nil? (:date_completed transfer))
            [:div {:class "form-group"}
              (label "account-to" "Account To")
              [:select {:name "account_to" :class "form-control" :id "account-to"}
                       [:option {:value ""} "Select..."]
                       (map #(vector :option {:value (:id %)} (:name %)) accounts)]])]])))
