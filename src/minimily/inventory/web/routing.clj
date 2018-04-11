(ns minimily.inventory.web.routing
  (:require [compojure.core                         :as core]
            [minimily.inventory.web.ctrl.inventory  :as inventory-ctrl]
            [minimily.inventory.web.ctrl.location   :as location-ctrl]
            [minimily.inventory.web.ctrl.collection :as collection-ctrl]
            [minimily.inventory.web.ctrl.good       :as good-ctrl]))

(defn routes []
  (core/routes
    (core/context "/inventory" []
      (core/GET  "/"         {session :session} 
                             (inventory-ctrl/view-inventory session))
      
      (core/context "/locations" []
        (core/GET  "/"         {session :session}
                               (location-ctrl/view-locations session))
        (core/GET  "/new"      {session :session}
                               (location-ctrl/new-location session))
        (core/POST "/save"     {session :session params :params} 
                               (location-ctrl/save-location session params))
        (core/POST "/delete"   {params :params}
                               (location-ctrl/delete-location params))
        (core/GET  "/:id"      {session :session {id :id} :params}
                               (location-ctrl/view-location session id))
        (core/GET  "/:id/edit" {session :session {id :id} :params}
                               (location-ctrl/edit-location session id)))

      (core/context "/collections" []
        (core/GET  "/"         {session :session}
                               (collection-ctrl/view-collections session))
        (core/GET  "/new"      {session :session}
                               (collection-ctrl/new-collection session))
        (core/POST "/save"     {session :session params :params}
                               (collection-ctrl/save-collection session params))
        (core/POST "/delete"   {params :params}
                               (collection-ctrl/delete-collection params))
        (core/GET  "/:id"      {session :session {id :id} :params}
                               (collection-ctrl/view-collection session id))
        (core/GET  "/:id/edit" {session :session {id :id} :params}
                               (collection-ctrl/edit-collection session id)))

      (core/context "/goods" []
          (core/GET  "/"         {session :session {location :location collection :collection} :params}
                                 (good-ctrl/view-goods session location collection))
          (core/GET  "/new"      {session :session {folder :folder} :params}
                                 (good-ctrl/new-good session))
          (core/POST "/save"     {session :session params :params} 
                                 (good-ctrl/save-good session params))
          (core/POST "/delete"   {params :params}
                                 (good-ctrl/delete-good params))
          (core/GET  "/:id"      {session :session {id :id} :params}
                                 (good-ctrl/view-good session id))
          (core/GET  "/:id/edit" {session :session {id :id} :params}
                                 (good-ctrl/edit-good session id))))))