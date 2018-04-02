(ns minimily.documents.web.routing
  (:require [compojure.core                     :as core]
            [minimily.documents.web.ctrl.folder :as folder-ctrl]))

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
                             (folder-ctrl/edit-folder session id)))))