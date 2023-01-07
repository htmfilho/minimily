(ns minimily.utils.date
  (:require [clj-time.format :as timef]
            [clj-time.coerce :as timec])
  (:import (java.text SimpleDateFormat)
           (java.util Date)))

(defn today []
  (new Date))

(defn to-string [date format]
  (if (nil? date)
    ""
    (.format (SimpleDateFormat. format) date)))

(defn to-date [str-date format]
  (let [str-date (str str-date " 12:00:00 -0500")
        format   (str format " HH:mm:ss Z")]
    (->> str-date
         (timef/parse (timef/formatter format))
         (timec/to-sql-date))))