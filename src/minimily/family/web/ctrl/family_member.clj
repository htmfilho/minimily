(ns minimily.family.web.ctrl.family-member
  (:require [ring.util.response                  :refer [redirect]]
            [minimily.family.web.ui.family       :refer [family-page]]
            [minimily.family.web.ui.family-form  :refer [family-form-page]]
            [minimily.family.model.family        :as family-model]
            [minimily.family.model.family-member :as family-member-model]))

(defn view-family-member [session]
  (let [family (family-model/find-family-organizer (:profile-id session))]
    (family-page session family)))

(defn new-family-member [session]
  (family-form-page session))

(defn edit-family-member [session id]
  (let [family-id (Integer/parseInt id)
        family    (family-model/get-it (:profile-id session) family-id)]
    (family-form-page session family)))

(defn save-family-member [session params]
  (let [family (conj params {:profile (:profile-id session)})
        id (family-model/save family)]
    (redirect "/family")))

(defn delete-family-member [session params]
  (let [id (:id params)]
    (family-model/delete-it (:profile-id session) id)
    (redirect "/family")))