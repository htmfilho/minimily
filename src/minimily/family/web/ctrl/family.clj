(ns minimily.family.web.ctrl.family
  (:require [ring.util.response                  :refer [redirect]]
            [minimily.family.web.ui.family       :refer [family-page]]
            [minimily.family.web.ui.family-form  :refer [family-form-page]]
            [minimily.family.model.family        :as family-model]
            [minimily.family.model.family-member :as family-member-model]))

(defn view-family [session]
  (let [family (family-model/find-family-organizer (:user-id session))]
    (family-page session family)))

(defn new-family [session]
  (family-form-page session))

(defn edit-family [session id]
  (let [family-id (Integer/parseInt id)
        family    (family-model/get-it (:user-id session) family-id)]
    (family-form-page session family)))

(defn save-family [session params]
  (let [family (conj params {:profile (:user-id session)})
        id (family-model/save family)]
    (redirect "/family")))

(defn delete-family [session params]
  (let [id (:id params)]
    (family-model/delete-it (:user-id session) id)
    (redirect "/family")))