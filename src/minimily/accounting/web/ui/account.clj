(ns minimily.accounting.web.ui.account
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn account-page [session account & [transactions]]
  (http-headers
    (layout session "Account"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/accounts/delete"]
            (back-button "/accounts")
            (str "&nbsp;")
            (edit-button (str "/accounts/" (:id account) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id account))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-3"} (show-field "Name" account :name)]
            [:div {:class "col-md-3"} (show-field "Number" account :number)]
            [:div {:class "col-md-2"} (show-field "Balance" account :balance)]
            [:div {:class "col-md-2"} (show-field "Currency" account :currency)]
            [:div {:class "col-md-2"} (show-field "Debit Limit" account :debit_limit)]]
          (let [percentage-used-credit (:percentage-used-credit account)
                severity (cond (< percentage-used-credit 30) "bg-success"
                               (and (>= percentage-used-credit 30) (< percentage-used-credit 50)) "bg-info"
                               (and (>= percentage-used-credit 50) (< percentage-used-credit 80)) "bg-warning"
                               :else "bg-danger")]
            (when (> percentage-used-credit 0)
              [:div {:class "progress"}
                [:div {:class (format "progress-bar %s" severity) :role "progressbar" 
                      :style (format "width: %d%%" percentage-used-credit) 
                      :aria-valuenow (format "%d" percentage-used-credit) 
                      :aria-valuemin "0" :aria-valuemax "100"} (format "%d%%" percentage-used-credit)]]))]]
      [:br]

      [:ul {:class "nav nav-tabs" :id "account-tabs" :role "tablist"}
        [:li {:class "nav-item"}
          [:a {:class "nav-link active" :id "transactions-tab" :data-toggle "tab"
               :href "#transactions-panel" :role "tab" :aria-controls "transactions-panel"
               :aria-selected "true"} "Transactions"]]
        [:li {:class "nav-item" }
          [:a {:class "nav-link" :id "history-tab" :data-toggle "tab"
               :href "#history-panel" :role "tab" :aria-controls "history-panel"
               :aria-selected "false"} "History"]]]

      [:div {:class "tab-content" :id "myTabContent"}
        [:div {:class "tab-pane fade show active" :id "transactions-panel" :role "tabpanel"
               :aria-labelledby "transactions-tab"}
          [:div {:class "card"}
            [:div {:class "card-header"}
              [:div {:class "btn-group" :role "group"}
                [:button {:id "btnGroupDrop" :type "button" :class "btn btn-secondary dropdown-toggle" :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"}
                  "New"]
                [:div {:class "dropdown-menu" :aria-labelledby "btnGroupDrop"}
                  [:a {:href (str "/accounts/" (:id account) "/transactions/new")
                      :class "dropdown-item"} "Transaction"]
                  [:a {:href (str "/accounts/" (:id account) "/transfer")
                      :class "dropdown-item"} "Transfer"]]]]
            [:table {:class "table table-striped"}
              [:thead
                [:tr
                  [:th "Description"]
                  [:th "Type"]
                  [:th {:style "text-align: right;"} (str "Amount" " (" (:currency account) ")")]
                  [:th "Date"]
                  [:th ""]]]
              [:tbody
                (map #(vector :tr [:td [:a {:href (str "/accounts/" (:id account) "/transactions/" (:id %))}
                                          (:description %)]]
                                  [:td (if (> (:type %) 0) "Credit" "Debit")]
                                  [:td {:style "text-align: right;"}
                                        (if (< (:type %) 0)
                                          [:span {:class "debit"} (:amount %)]
                                          [:span {:class "credit"} (:amount %)])]
                                  [:td (to-string (:date_transaction %) "MMM dd, yyyy")]
                                  [:td (when (:account_transfer %) [:a {:href (str "/accounts/" (:account_transfer %))} [:i {:class "fas fa-link"}]])]) transactions)]]]]

        [:div {:class "tab-pane fade show" :id "history-panel" :role "tabpanel"
               :aria-labelledby "history-tab"}
          [:div {:id "balance-history-chart" :style "width: 800px;height:400px;"}]]])))
