(ns minimily.inventory.web.ctrl.collection
  (:require [ring.util.response                        :refer [redirect]]
            [minimily.inventory.web.ui.collection      :as collection-view]
            [minimily.inventory.model.collection       :as collection-model]
            [minimily.inventory.model.good             :as good-model]))

(defn view-collections [session]
  (let [collections (collection-model/find-all (:profile-id session))]
    (collection-view/collections-page session collections)))

(defn view-collection [session id]
  (let [collection-id (Integer/parseInt id)
        collection    (collection-model/get-it (:profile-id session) collection-id)
        goods         (good-model/find-by-collection (:profile-id session) collection-id)]
    (collection-view/collection-page session collection goods)))

(defn new-collection [session]
  (collection-view/collection-form-page session))

(defn edit-collection [session id]
  (let [collection-id (Integer/parseInt id)
        collection    (collection-model/get-it (:profile-id session) collection-id)]
    (collection-view/collection-form-page session collection)))

(defn save-collection [session params]
  (let [collection (conj params {:profile (:profile-id session)})
        id (collection-model/save collection)]
    (redirect "/inventory/collections")))

(defn delete-collection [session params]
  (let [id (:id params)]
    (collection-model/delete-it (:profile-id session) id)
    (redirect "/inventory/collection")))