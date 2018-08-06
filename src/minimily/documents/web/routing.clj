(ns minimily.documents.web.routing
  (:require [compojure.core                       :as url]
            [ring.middleware.params               :refer [wrap-params]]
            [minimily.documents.web.ctrl.folder   :as folder-ctrl]
            [minimily.documents.web.ctrl.document :as document-ctrl]))

(defn routes []
  (url/routes
    (url/context "/folders" []
      (url/GET  "/"         {session :session} 
                             (folder-ctrl/view-parent-folders session))
      (url/GET  "/new"      {session :session {parent :parent} :params} 
                             (folder-ctrl/new-folder session parent))
      (url/POST "/save"     {session :session params :params} 
                             (folder-ctrl/save-folder session params))
      (url/POST "/delete"   {session :session params :params} 
                             (folder-ctrl/delete-folder session params))
      (url/GET  "/:id"      {session :session {id :id} :params}
                             (folder-ctrl/view-folder session id))
      (url/GET  "/:id/edit" {session :session {id :id} :params}
                             (folder-ctrl/edit-folder session id))
      
      (url/context "/:folder/documents" []
        (url/GET  "/new"      {session :session {folder :folder} :params}
                               (document-ctrl/new-document session folder))
        (url/POST "/save"     {session :session params :params} 
                               (document-ctrl/save-document session params))
        (url/POST "/delete"   {session :session params :params}
                               (document-ctrl/delete-document session params))
        (url/GET  "/:id"      {session :session {id :id} :params}
                               (document-ctrl/view-document session id))
        (url/GET  "/:id/edit" {session :session {id :id} :params}
                               (document-ctrl/edit-document session id))
        (url/GET  "/:id/file" {session :session {id :id} :params}
                               (document-ctrl/download-document session id))))))