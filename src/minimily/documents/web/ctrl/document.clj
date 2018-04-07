(ns minimily.documents.web.ctrl.document
  (:require [ring.util.response                      :refer [redirect 
                                                             file-response 
                                                             header
                                                             content-type]]
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
  (let [document (document-model/get-it id)
        folder   (folder-model/get-it (:folder document))]
    (document-form-page session folder document)))

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
                     (conj {:file_store_path (str "documents/" (:filename file))}))]
    (document-model/save document (:tempfile file))
    (println folder)
    (redirect (str "/folders/" folder))))

(defn delete-document [params]
  (let [id     (:id params)
        folder (:folder params)]
    (document-model/delete-it id)
    (redirect (str "/folders/" folder))))

(defn download-document [id]
  (let [document (document-model/get-file id)]
    (content-type {:status 200
                   :headers {}
                   :body (:input-stream document)} (:content-type 
                                                      (:object-metadata document)))))

;(defn download-document [id]
;  (-> (file-response "/path/to/my/document/ugly_file_name.pdf")
;      (header "Content-Disposition" "attachment; filename=\"nice_file_name.pdf\"")
;      (content-type "application/pdf")
;      (header "Content-Length" 3746220)))