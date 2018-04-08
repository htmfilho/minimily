(ns minimily.inventory.web.routing
  (:require [compojure.core                        :as core]
            [minimily.inventory.web.ctrl.inventory :as inventory-ctrl]))

(defn routes []
  (core/routes
    (core/context "/inventory" []
      (core/GET  "/"         {session :session} 
                             (inventory-ctrl/view-inventory session)))))
      
;      (core/context "/locations" []
;        (core/GET  "/new"      {session :session {folder :folder} :params}
;                               (document-ctrl/new-document session folder))
;        (core/POST "/save"     {session :session params :params} 
;                               (document-ctrl/save-document session params))
;        (core/POST "/delete"   {params :params}
;                               (document-ctrl/delete-document params))
;        (core/GET  "/:id"      {session :session {id :id} :params}
;                               (document-ctrl/view-document session id))
;        (core/GET  "/:id/edit" {session :session {id :id} :params}
;                               (document-ctrl/edit-document session id)))
;                               
;      (core/context "/collections" []
;        (core/GET  "/new"      {session :session {folder :folder} :params}
;                               (document-ctrl/new-document session folder))
;        (core/POST "/save"     {session :session params :params} 
;                               (document-ctrl/save-document session params))
;        (core/POST "/delete"   {params :params}
;                               (document-ctrl/delete-document params))
;        (core/GET  "/:id"      {session :session {id :id} :params}
;                               (document-ctrl/view-document session id))
;        (core/GET  "/:id/edit" {session :session {id :id} :params}
;                               (document-ctrl/edit-document session id))
;                               
;        (core/context "/:collection/goods" []
;          (core/GET  "/new"      {session :session {folder :folder} :params}
;                                (document-ctrl/new-document session folder))
;          (core/POST "/save"     {session :session params :params} 
;                                (document-ctrl/save-document session params))
;          (core/POST "/delete"   {params :params}
;                                (document-ctrl/delete-document params))
;          (core/GET  "/:id"      {session :session {id :id} :params}
;                                (document-ctrl/view-document session id))
;          (core/GET  "/:id/edit" {session :session {id :id} :params}
;                                (document-ctrl/edit-document session id)))))))