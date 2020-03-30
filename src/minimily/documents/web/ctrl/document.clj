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
        document    (document-model/get-it (:profile-id session) document-id)
        folder      (folder-model/get-it (:profile-id session) (:folder document))
        path        (document-model/find-path (:profile-id session) document-id)]
    (document-page session document folder path)))

(defn new-document [session folder-id]
  (let [folder (folder-model/get-it (:profile-id session) folder-id)]
    (document-form-add session folder)))

(defn edit-document [session id]
  (let [document (document-model/get-it (:profile-id session) id)
        folder   (folder-model/get-it (:profile-id session) (:folder document))]
    (document-form-edit session folder document)))

(defn save-document [session params]
  (if (:id params)
    (let [document (-> {}
                       (conj {:id (:id params)})
                       (conj {:title (:title params)})
                       (conj {:description (:description params)}))]
      (document-model/save document)
      (redirect (str "/folders/" (:folder params))))
    (let [folder (Integer/parseInt (:folder params))
        file (:file params)
        document (-> {} 
                     (conj {:folder folder})
                     (conj {:title (if (empty? (:title params)) 
                                     (:filename file) 
                                     (:title params))})
                     (conj {:description (:description params)})
                     (conj {:file_original_name (:filename file)})
                     (conj {:file_format (:content-type file)})
                     (conj {:file_size (:size file)})
                     (conj {:file_store_path (str (folder-model/path (:profile-id session) folder) "/" (s/tech (:filename file)))})
                     (conj {:profile (:profile-id session)}))]
      (document-model/save document (:tempfile file))
      (redirect (str "/folders/" folder) :see-other))))

(defn save-and-new-document [session params]
  (if (:id params)
    (let [document (-> {}
                       (conj {:id (:id params)})
                       (conj {:title (:title params)})
                       (conj {:description (:description params)}))]
      (document-model/save document)
      (redirect (str "/folders/" (:folder params))))
    (let [folder (Integer/parseInt (:folder params))
        file (:file params)
        document (-> {} 
                     (conj {:folder folder})
                     (conj {:title (if (empty? (:title params)) 
                                     (:filename file) 
                                     (:title params))})
                     (conj {:description (:description params)})
                     (conj {:file_original_name (:filename file)})
                     (conj {:file_format (:content-type file)})
                     (conj {:file_size (:size file)})
                     (conj {:file_store_path (str (folder-model/path (:profile-id session) folder) "/" (s/tech (:filename file)))})
                     (conj {:profile (:profile-id session)}))]
      (document-model/save document (:tempfile file))
      (redirect (str "/folders/" folder "/documents/new") :see-other))))

(defn delete-document [session params]
  (let [document-id (Integer/parseInt (:id params))]
    (document-model/delete-it (:profile-id session) document-id)
    (redirect (str "/folders/" (:folder params)))))

(defn content-disposition [file-format file-name]
  (str (get {"image/jpeg"      "inline"
             "application/pdf" "inline"} file-format "attachment")
       "; filename=\"" file-name "\""))

(defn download-document [session id]
  (let [document    (document-model/get-it (:profile-id session) id)
        s3-document (document-model/get-file document)]
    (content-type {:status 200
                   :headers {"Content-Disposition" (content-disposition (:file_format document) (:file_original_name document))}
                   :body (:input-stream s3-document)} 
                  (:content-type (:file-format document)))))