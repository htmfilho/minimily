(ns minimily.utils.date
  (:require [clj-time.format :as timef]
            [clj-time.coerce :as timec])
  (:import (java.text SimpleDateFormat)))

(defn to-string [date format]
  (.format (SimpleDateFormat. format) date))

(defn to-timestamp [str-date format]
  (->> str-date
       (timef/parse (timef/formatter format))
       (timec/to-timestamp)))