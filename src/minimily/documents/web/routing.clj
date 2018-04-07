(ns minimily.documents.web.routing
  (:require [compojure.core                       :as core]
            [ring.middleware.params               :refer [wrap-params]]
            [ring.middleware.multipart-params     :refer [wrap-multipart-params]]
            [minimily.documents.web.ctrl.folder   :as folder-ctrl]
            [minimily.documents.web.ctrl.document :as document-ctrl]))

(defn routes []
  (core/routes
    (core/context "/folders" []
      (core/GET  "/"         {session :session} 
                             (folder-ctrl/view-parent-folders session))
      (core/GET  "/new"      {session :session {parent :parent} :params} 
                             (folder-ctrl/new-folder session parent))
      (core/POST "/save"     {session :session params :params} 
                             (folder-ctrl/save-folder session params))
      (core/POST "/delete"   {params :params} 
                             (folder-ctrl/delete-folder params))
      (core/GET  "/:id"      {session :session {id :id} :params}
                             (folder-ctrl/view-folder session id))
      (core/GET  "/:id/edit" {session :session {id :id} :params}
                             (folder-ctrl/edit-folder session id))
      
      (core/context "/:folder/documents" []
        (core/GET  "/new"      {session :session {folder :folder} :params}
                               (document-ctrl/new-document session folder))
        (core/POST "/save"     {session :session params :params} 
                               (document-ctrl/save-document session params))
        (core/POST "/delete"   {params :params}
                               (document-ctrl/delete-document params))
        (core/GET  "/:id"      {session :session {id :id} :params}
                               (document-ctrl/view-document session id))
        (core/GET  "/:id/edit" {session :session {id :id} :params}
                               (document-ctrl/edit-document session id))
        (core/GET  "/:id/file" {session :session {id :id} :params}
                               (document-ctrl/download-document id))))))