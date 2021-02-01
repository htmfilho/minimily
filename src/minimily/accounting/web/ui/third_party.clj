(ns minimily.accounting.web.ui.third-party
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [show-field back-button edit-button]]
            [minimily.utils.date        :refer [to-string]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn third-party-page [session third-party]
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
          (show-field "Name" third-party :name)]])))
