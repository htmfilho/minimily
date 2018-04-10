(ns minimily.inventory.web.ctrl.location
  (:require [ring.util.response                      :refer [redirect]]
            [minimily.inventory.web.ui.location      :refer [location-page]]
            [minimily.inventory.web.ui.locations     :refer [locations-page]]
            [minimily.inventory.web.ui.location-form :refer [location-form-page]]
            [minimily.inventory.model.location       :as location-model]
            [minimily.inventory.model.good           :as good-model]))

(defn view-locations [session]
  (let [locations (location-model/find-all)]
    (locations-page session locations)))

(defn view-location [session id]
  (let [location (location-model/get-it id)
        goods     (good-model/find-by-location id)]
    (location-page session location goods)))

(defn new-location [session]
  (location-form-page session))

(defn edit-location [session id]
  (let [location (location-model/get-it id)]
    (location-form-page session location)))

(defn save-location [session params]
  (let [id (location-model/save params)]
    (redirect "/inventory/locations")))

(defn delete-location [params]
  (let [id (:id params)]
    (location-model/delete-it id)
    (redirect "/inventory/locations")))