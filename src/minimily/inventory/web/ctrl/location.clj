(ns minimily.inventory.web.ctrl.location
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.inventory.web.ui.location      :refer [location-page]]
            [minimily.inventory.web.ui.locations     :refer [locations-page]]
            [minimily.inventory.web.ui.location-form :refer [location-form-page]]
            [minimily.inventory.model.location       :as location-model]
            [minimily.inventory.model.good           :as good-model]))

(defn view-locations [session]
  (let [locations (location-model/find-all (:user-id session))]
    (locations-page session locations)))

(defn view-location [session id]
  (let [location-id (Integer/parseInt id)
        location    (location-model/get-it (:user-id session) location-id)
        goods       (good-model/find-by-location (:user-id session) location-id)]
    (location-page session location goods)))

(defn new-location [session]
  (location-form-page session))

(defn edit-location [session id]
  (let [location-id (Integer/parseInt id)
        location    (location-model/get-it (:user-id session) location-id)]
    (location-form-page session location)))

(defn save-location [session params]
  (let [location (conj params {:profile (:user-id session)})
        id (location-model/save location)]
    (redirect "/inventory/locations")))

(defn delete-location [session params]
  (let [id (Integer/parseInt (:id params))]
    (location-model/delete-it (:user-id session) id)
    (redirect "/inventory/locations")))