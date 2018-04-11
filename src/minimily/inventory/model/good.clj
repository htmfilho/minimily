(ns minimily.inventory.model.good
  (:require [minimily.utils.database :as db]))

(def table :good)

(defn find-by-location [location-id]
  (db/find-records (str "select * from good where location = " location-id)))

(defn find-by-collection [collection-id]
  (db/find-records (str "select * from good where collection = " collection-id)))

(defn find-by-criteria [location-id collection-id]
  (let [location?   (not (empty? location-id))
        collection? (not (empty? collection-id))]
    (if (or location? collection?)
      (let [query (str "select * from good where "
                      (when location? (str "location = " location-id))
                      (when (and location? collection?) " and ")
                      (when collection? (str "collection = " collection-id)))]
        (db/find-records query))
      []))
  )

(defn get-it [id]
  (let [goods (db/find-records (str "select g.id, g.name, g.description, g.quantity, g.value, 
                                            l.id as location_id, l.name as location, 
                                            c.id as collection_id, c.name as collection 
                                     from good g left join location l on g.location = l.id
                                                 left join collection c on g.collection = c.id
                                     where g.id = " id))]
    (if (empty? goods)
      nil
      (first goods))))

(defn save [good]
  (db/save-record table good))

(defn delete-it [id]
  (db/delete-record table id))