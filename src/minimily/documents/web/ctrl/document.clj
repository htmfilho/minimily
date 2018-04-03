(ns minimily.documents.web.ctrl.document
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.documents.web.ui.document-form :refer [document-form-page]]
            [minimily.documents.web.ui.document      :refer [document-page]]
            [minimily.documents.model.folder         :as folder-model]
            [minimily.documents.model.document       :as document-model]))

(defn view-document [session id]
  (let [document (document-model/get-it id)
        folder   (folder-model/get-it (:folder document))
        path     (document-model/find-path id)]
    (document-page session document folder path)))

(defn new-document [session folder-id]
  (let [folder (folder-model/get-it folder-id)]
    (document-form-page session folder)))

(defn edit-document [session id]
  (let [document (document-model/get-it id)]
    (document-form-page session document)))

(defn save-document [session document]
  (let [folder (Integer/parseInt (:folder document))
        document (conj document {:folder folder})
        id (document-model/save document)]
    (redirect (str "/folders/" folder))))

(defn delete-document [params]
  (let [id     (:id params)
        folder (:folder params)]
    (document-model/delete-it id)
    (redirect (str "/folders" folder))))