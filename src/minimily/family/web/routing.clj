(ns minimily.family.web.routing
  (:require [compojure.core                         :as url]
            [ring.middleware.params                 :refer [wrap-params]]
            [minimily.family.web.ctrl.family        :as family-ctrl]
            [minimily.family.web.ctrl.family-member :as family-member-ctrl]))

(defn routes []
  (url/routes
    (url/context "/family" []
      (url/GET "/"
               {session :session} 
               (family-ctrl/view-family session))
      
      (url/GET "/new"
               {session :session {parent :parent} :params} 
               (family-ctrl/new-family session))
      
      (url/GET "/edit"
               {session :session}
               (family-ctrl/edit-family session))
      
      (url/POST "/save"
                {session :session params :params} 
                (family-ctrl/save-family session params))
      
      (url/POST "/delete"
                {session :session params :params} 
                (family-ctrl/delete-family session params))
      
      (url/context "/family/members" []
        (url/GET "/new"
                 {session :session {family :family} :params}
                 (family-member-ctrl/new-family-member session family))
        
        (url/POST "/save"
                  {session :session params :params} 
                  (family-member-ctrl/save-family-member session params))
        
        (url/POST "/delete"
                  {session :session params :params}
                  (family-member-ctrl/delete-family-member session params))
        
        (url/GET "/:id"
                  {session :session {id :id} :params}
                  (family-member-ctrl/view-family-member session id))
        
        (url/GET "/:id/edit"
                 {session :session {id :id} :params}
                 (family-member-ctrl/edit-family-member session id))))))