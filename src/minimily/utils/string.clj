(ns minimily.utils.string
  (:require [clojure.string :as s]))

(defn tech [string]
  (s/lower-case (-> string
                    (s/replace "_" "-")
                    (s/replace "&" "")
                    (s/replace #"[\s]+" "-"))))