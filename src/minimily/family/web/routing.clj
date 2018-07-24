(ns minimily.family.web.routing
  (:require [compojure.core                         :as core]
            [ring.middleware.params                 :refer [wrap-params]]
            [minimily.family.web.ctrl.family        :as family-ctrl]
            [minimily.family.web.ctrl.family-member :as family-member-ctrl]))

(defn routes []
  (core/routes
    (core/context "/family" []
      (core/GET  "/"         {session :session} 
                             (family-ctrl/view-family session))
      (core/GET  "/new"      {session :session {parent :parent} :params} 
                             (family-ctrl/new-family session))
      (core/GET  "/edit"     {session :session}
                             (family-ctrl/edit-family session))
      (core/POST "/save"     {session :session params :params} 
                             (family-ctrl/save-family session params))
      (core/POST "/delete"   {session :session params :params} 
                             (family-ctrl/delete-family session params))
      
      (core/context "/family/members" []
        (core/GET  "/new"      {session :session {family :family} :params}
                               (family-member-ctrl/new-family-member session family))
        (core/POST "/save"     {session :session params :params} 
                               (family-member-ctrl/save-family-member session params))
        (core/POST "/delete"   {session :session params :params}
                               (family-member-ctrl/delete-family-member session params))
        (core/GET  "/:id"      {session :session {id :id} :params}
                               (family-member-ctrl/view-family-member session id))
        (core/GET  "/:id/edit" {session :session {id :id} :params}
                               (family-member-ctrl/edit-family-member session id))))))