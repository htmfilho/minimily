(ns minimily.inventory.web.ctrl.location
  (:require [minimily.inventory.web.ui.locations :refer [locations-page]]
            [minimily.inventory.model.location   :as location-model]))

(defn view-locations [session]
  (let [locations (location-model/find-all)]
    (locations-page session locations)))