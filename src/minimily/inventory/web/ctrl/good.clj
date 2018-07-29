(ns minimily.inventory.web.ctrl.good
  (:require [ring.util.response                  :refer [redirect]]
            [minimily.inventory.web.ui.good      :refer [good-page]]
            [minimily.inventory.web.ui.goods     :refer [goods-page]]
            [minimily.inventory.web.ui.good-form :refer [good-form-page]]
            [minimily.inventory.model.collection :as collection-model]
            [minimily.inventory.model.location   :as location-model]
            [minimily.inventory.model.good       :as good-model]))

(defn view-goods [session location collection]
  (let [goods       (good-model/find-by-criteria (:user-id session) location collection)
        locations   (if (empty? location)
                      (location-model/find-all (:user-id session))
                      (map #(if (= (:id %) (Integer/parseInt location))
                            (conj % {:selected true}) 
                            (conj % {:selected false})) 
                           (location-model/find-all (:user-id session))))
        collections (if (empty? collection)
                      (collection-model/find-all (:user-id session))
                      (map #(if (= (:id %) (Integer/parseInt collection))
                            (conj % {:selected true}) 
                            (conj % {:selected false})) 
                           (collection-model/find-all (:user-id session))))]
    (goods-page session goods locations collections)))

(defn view-good [session id]
  (let [good-id (Integer/parseInt id)
        good    (good-model/get-it (:user-id session) good-id)]
    (good-page session good)))

(defn new-good [session]
  (let [locations   (location-model/find-all (:user-id session))
        collections (collection-model/find-all (:user-id session))]
    (good-form-page session locations collections)))

(defn edit-good [session id]
  (let [good-id     (Integer/parseInt id)
        good        (good-model/get-it (:user-id session) good-id)
        locations   (map #(if (= (:id %) (:location_id good))
                            (conj % {:selected true}) 
                            (conj % {:selected false})) 
                         (location-model/find-all (:user-id session)))
        collections (map #(if (= (:id %) (:collection_id good))
                            (conj % {:selected true}) 
                            (conj % {:selected false})) 
                         (collection-model/find-all (:user-id session)))]
    (println good)
    (good-form-page session locations collections good)))

(defn save-good [session params]
  (let [good (-> params
                 (conj {:quantity   (BigDecimal. (:quantity params))})
                 (conj {:value      (BigDecimal. (:value params))})
                 (conj {:location   (Integer/parseInt (:location params))})
                 (conj {:collection (Integer/parseInt (:collection params))})
                 (conj {:profile    (:user-id session)}))
        id (good-model/save good)]
    (redirect (str "/inventory/goods?location=" (:location params) "&collection=" (:collection params)))))

(defn delete-good [session params]
  (let [id         (Integer/parseInt (:id params))
        location   (Integer/parseInt (:location params))
        collection (Integer/parseInt (:collection params))]
    (good-model/delete-it (:user-id session) id)
    (redirect (format "/inventory/goods?location=%d&collection=%d" location collection))))