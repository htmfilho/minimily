(ns minimily.inventory.web.routing
  (:require [compojure.core                         :as url]
            [minimily.inventory.web.ctrl.inventory  :as inventory-ctrl]
            [minimily.inventory.web.ctrl.location   :as location-ctrl]
            [minimily.inventory.web.ctrl.collection :as collection-ctrl]
            [minimily.inventory.web.ctrl.good       :as good-ctrl]))

(defn routes []
  (url/routes
    (url/context "/inventory" []
      (url/GET  "/"         {session :session} 
                             (inventory-ctrl/view-inventory session))
      
      (url/context "/locations" []
        (url/GET "/"
                 {session :session}
                 (location-ctrl/view-locations session))
        
        (url/GET "/new"
                 {session :session}
                 (location-ctrl/new-location session))
        
        (url/POST "/save"
                  {session :session params :params} 
                  (location-ctrl/save-location session params))
        
        (url/POST "/delete"
                  {session :session params :params}
                  (location-ctrl/delete-location session params))
        
        (url/GET "/:id"
                 {session :session {id :id} :params}
                 (location-ctrl/view-location session id))
        
        (url/GET "/:id/edit"
                 {session :session {id :id} :params}
                 (location-ctrl/edit-location session id)))

      (url/context "/collections" []
        (url/GET "/"
                 {session :session}
                 (collection-ctrl/view-collections session))
        
        (url/GET "/new"
                 {session :session}
                 (collection-ctrl/new-collection session))
        
        (url/POST "/save"
                  {session :session params :params}
                  (collection-ctrl/save-collection session params))
        
        (url/POST "/delete"
                  {session :session params :params}
                  (collection-ctrl/delete-collection params))
        
        (url/GET "/:id"
                 {session :session {id :id} :params}
                 (collection-ctrl/view-collection session id))
        
        (url/GET "/:id/edit" 
                 {session :session {id :id} :params}
                 (collection-ctrl/edit-collection session id)))

      (url/context "/goods" []
          (url/GET "/"
                   {session :session {location :location collection :collection} :params}
                   (good-ctrl/view-goods session location collection))
          
          (url/GET "/new"
                   {session :session {folder :folder} :params}
                   (good-ctrl/new-good session))
          
          (url/POST "/save"
                    {session :session params :params} 
                    (good-ctrl/save-good session params))
          
          (url/POST "/delete"
                    {session :session params :params}
                    (good-ctrl/delete-good session params))
          
          (url/GET "/:id"
                   {session :session {id :id} :params}
                   (good-ctrl/view-good session id))
          
          (url/GET "/:id/edit"
                   {session :session {id :id} :params}
                   (good-ctrl/edit-good session id))))))