(ns minimily.persistence_model)

(defprotocol Entity
  (save [record])
  (remove [record])
  (get-one [id]))
