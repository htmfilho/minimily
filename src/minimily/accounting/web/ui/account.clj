(ns minimily.accounting.web.ui.account
  (:require [hiccup.form                :refer [form-to label submit-button text-field
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field show-field-link back-button edit-button checkbox]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn accounts-page [session active-accounts inactive-accounts third-party-accounts]
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
                                  [:td (:currency %)]) inactive-accounts)]]]]
                
        [:div {:class "tab-pane fade show" :id "third-parties-panel" :role "tabpanel"
               :aria-labelledby "third-parties-tab"}
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
                                  [:td (:currency %)]) third-party-accounts)]]]]])))

(defn account-page [session account third-party & [transactions]]
  (http-headers
    (layout session "Account"
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post "/accounting/accounts/delete"]
            (back-button "/accounting/accounts")
            (str "&nbsp;")
            (edit-button (str "/accounting/accounts/" (:id account) "/edit"))
            (str "&nbsp;")
            (hidden-field "id" (:id account))
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          [:div {:class "row"}
            [:div {:class "col-md-3"} (show-field "Name" account :name)]
            [:div {:class "col-md-3"} (show-field-link "Party" third-party :name (str "/accounting/third_parties/" (:id third-party)))]
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
                  [:a {:href (str "/accounting/accounts/" (:id account) "/transactions/new")
                      :class "dropdown-item"} "Transaction"]
                  [:a {:href (str "/accounting/accounts/" (:id account) "/transfer")
                      :class "dropdown-item"} "Transfer"]]]]
            [:table {:class "table table-striped"}
              [:thead
                [:tr
                  [:th "Description"]
                  [:th "Type"]
                  [:th {:style "text-align: right;"} (str "Amount" (if (:currency account) 
                                                                     (str " (" (:currency account) ")")
                                                                     ""))]
                  [:th "Date"]
                  [:th ""]]]
              [:tbody
                (map #(vector :tr [:td [:a {:href (str "/accounting/accounts/" (:id account) "/transactions/" (:id %))}
                                          (:description %)]]
                                  [:td (if (> (:type %) 0) "Credit" "Debit")]
                                  [:td {:style "text-align: right;"}
                                        (if (< (:type %) 0)
                                          [:span {:class "debit"} (:amount %)]
                                          [:span {:class "credit"} (:amount %)])]
                                  [:td (to-string (:date_transaction %) "MMM dd, yyyy")]
                                  [:td (when (:account_transfer %) [:a {:href (str "/accounting/accounts/" (:account_transfer %))} [:i {:class "fas fa-link"}]])]) transactions)]]]]

        [:div {:class "tab-pane fade show" :id "history-panel" :role "tabpanel"
               :aria-labelledby "history-tab"}
          [:div {:id "balance-history-chart" :style "width: 800px;height:400px;"}]]])))

(defn account-form-page [session currencies third-parties & [account]]
  (http-headers
    (layout session "Account"
      (form-to [:post "/accounting/accounts/save"]
        (when account (hidden-field "id" (:id account)))
        [:div {:class "form-group"}
          (label "name" "Name")
          (text-field {:class "form-control" :id "name" :maxlength "100"} 
                      "name" 
                      (:name account))]
        [:div {:class "row"}
          [:div {:class "col-md-4"}
            [:div {:class "form-group"}
              (label "third-party" "Party")
              [:select {:class "form-control" :name "third_party"}
                       [:option {:value ""} "Select..."]
                       (map #(vector :option (if (:selected %)
                                               {:value (:id %) :selected "true"}
                                               {:value (:id %)}) (:name %)) third-parties)]]]
          [:div {:class "col-md-4"}
            [:div {:class "form-group"}
              (label "currency" "Currency")
              [:select {:name "currency" :class "form-control" :id "currency" :required "required"}
                       [:option {:value ""} "Select..."]
                       (map #(vector :option (if (:selected %) 
                                               {:value (:acronym %) :selected "true"}
                                               {:value (:acronym %)}) (str (:acronym %) " (" (:name %) ")")) currencies)]]]
          [:div {:class "col-md-2"}
            [:div {:class "form-group"}
              (label "debit_limit" "Debit Limit")
              (text-field {:class "form-control" :id "debit_limit"}
                          "debit_limit"
                          (:debit_limit account))]]
          [:div {:class "col-md-2"}
            (if (:active account)
              (checkbox "active" "Active" (:active account))
              (checkbox "active" "Active" true))]]
        (submit-button {:class "btn btn-primary"} "Submit")
        (str "&nbsp;")
        [:a {:class "btn btn-outline-secondary" :href (str "/accounting/accounts/" (:id account))} "Cancel"]))))