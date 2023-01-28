(ns minimily.utils.config
  (:require [clojure.edn         :as edn]
            [clojure.java.io     :as io]))

(defn copy-file [source-path dest-path]
  (io/copy (io/file source-path) 
           (io/file dest-path)))

(defn initialize []
  (copy-file "config/prod/config.edn.example" "config/prod/config.edn"))