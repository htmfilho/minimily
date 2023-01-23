(ns minimily.inventory.web.ctrl.location
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.inventory.web.ui.location      :as location-view]
            [minimily.inventory.model.location       :as location-model]
            [minimily.inventory.model.good           :as good-model]))

(defn view-locations [session]
  (let [locations (location-model/find-all (:profile-id session))]
    (location-view/locations-page session locations)))

(defn view-location [session id]
  (let [location-id (Integer/parseInt id)
        location    (location-model/get-it (:profile-id session) location-id)
        goods       (good-model/find-by-location (:profile-id session) location-id)]
    (location-view/location-page session location goods)))

(defn new-location [session]
  (location-view/location-form-page session))

(defn edit-location [session id]
  (let [location-id (Integer/parseInt id)
        location    (location-model/get-it (:profile-id session) location-id)]
    (location-view/location-form-page session location)))

(defn save-location [session params]
  (let [location (conj params {:profile (:profile-id session)})
        id (location-model/save location)]
    (redirect "/inventory/locations")))

(defn delete-location [session params]
  (let [id (Integer/parseInt (:id params))]
    (location-model/delete-it (:profile-id session) id)
    (redirect "/inventory/locations")))