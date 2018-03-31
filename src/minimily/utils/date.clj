(ns minimily.utils.date)

(defn to-string [date format]
  (.format (java.text.SimpleDateFormat. format) date))