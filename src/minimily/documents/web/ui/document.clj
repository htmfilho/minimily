(ns minimily.documents.web.ui.document
  (:require [hiccup.form                :refer [form-to submit-button 
                                                hidden-field]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.web.ui.bootstrap  :refer [back-button edit-button show-field]]
            [minimily.utils.web.wrapper :refer [http-headers]]))

(defn document-page [session document folder path]
  (http-headers
    (layout session [:span [:i {:class "far fa-folder-open"}] (str "&nbsp;") (:name document)]
      [:div {:class "card"}
        [:div {:class "card-header"}
          (form-to {:id "frm_delete"} [:post (str "/folders/" (:id folder) "/documents/delete")]
            (back-button (str "/folders/" (:id folder)))
            (str "&nbsp;")
            (edit-button (str "/folders/" (:id folder) "/documents/" (:id document) "/edit"))
            (hidden-field "id" (:id document))
            (str "&nbsp;")
            (submit-button {:id "bt_delete" :class "btn btn-danger"} "Delete"))]
        [:div {:class "card-body"}
          (show-field "Folder" folder :name)
          (show-field "Title" document :title)
          (show-field "Description" document :description)]])))
