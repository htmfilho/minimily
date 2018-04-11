(ns minimily.inventory.web.ctrl.collection
  (:require [ring.util.response                        :refer [redirect]]
            [minimily.inventory.web.ui.collection      :refer [collection-page]]
            [minimily.inventory.web.ui.collections     :refer [collections-page]]
            [minimily.inventory.web.ui.collection-form :refer [collection-form-page]]
            [minimily.inventory.model.collection       :as collection-model]
            [minimily.inventory.model.good             :as good-model]))

(defn view-collections [session]
  (let [collections (collection-model/find-all)]
    (collections-page session collections)))

(defn view-collection [session id]
  (let [collection (collection-model/get-it id)
        goods     (good-model/find-by-collection id)]
    (collection-page session collection goods)))

(defn new-collection [session]
  (collection-form-page session))

(defn edit-collection [session id]
  (let [collection (collection-model/get-it id)]
    (collection-form-page session collection)))

(defn save-collection [session params]
  (let [id (collection-model/save params)]
    (redirect "/inventory/collections")))

(defn delete-collection [params]
  (let [id (:id params)]
    (collection-model/delete-it id)
    (redirect "/inventory/collection")))