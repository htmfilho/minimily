(ns minimily.utils.string
  (:require [clojure.string :as s]))

(defn tech [string]
  (-> (s/lower-case string)
      (s/replace "_" "-")
      (s/replace #"&|'" "")
      (s/replace #"ã|à|á|â" "a")
      (s/replace "ç" "c")
      (s/replace #"[\s]+" "-")))