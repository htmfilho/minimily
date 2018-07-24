(ns minimily.documents.web.ctrl.document
  (:require [ring.util.response                      :refer [redirect 
                                                             file-response 
                                                             header
                                                             content-type]]
            [minimily.documents.web.ui.document-form :refer :all]
            [minimily.documents.web.ui.document      :refer [document-page]]
            [minimily.documents.model.folder         :as folder-model]
            [minimily.documents.model.document       :as document-model]
            [minimily.utils.string                   :as s]))

(defn view-document [session id]
  (let [document-id (Integer/parseInt id)
        document    (document-model/get-it (:user-id session) document-id)
        folder      (folder-model/get-it (:user-id session) (:folder document))
        path        (document-model/find-path (:user-id session) document-id)]
    (document-page session document folder path)))

(defn new-document [session folder-id]
  (let [folder (folder-model/get-it (:user-id session) folder-id)]
    (document-form-add session folder)))

(defn edit-document [session id]
  (let [document (document-model/get-it (:user-id session) id)
        folder   (folder-model/get-it (:user-id session) (:folder document))]
    (document-form-edit session folder document)))

(defn save-document [session params]
  (let [folder (Integer/parseInt (:folder params))
        file (:file params)
        document (-> {} 
                     (conj {:folder folder})
                     (conj {:title (:title params)})
                     (conj {:description (:description params)})
                     (conj {:file_original_name (:filename file)})
                     (conj {:file_format (:content-type file)})
                     (conj {:file_size (:size file)})
                     (conj {:file_store_path (str (folder-model/path (:user-id session) folder) "/" (s/tech (:filename file)))})
                     (conj {:profile (:user-id session)}))]
    (document-model/save document (:tempfile file))
    (redirect (str "/folders/" folder) :see-other)))

(defn delete-document [session params]
  (let [document-id (Integer/parseInt (:id params))]
    (document-model/delete-it (:user-id session) document-id)
    (redirect (str "/folders/" (:folder params)))))

(defn download-document [session id]
  (let [document (document-model/get-file (:user-id session) id)]
    (content-type {:status 200
                   :headers {}
                   :body (:input-stream document)} 
                  (:content-type (:object-metadata document)))))