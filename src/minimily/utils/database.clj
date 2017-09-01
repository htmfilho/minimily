(ns minimily.utils.database
  (:require [hikari-cp.core :refer :all]
            [clojure.java.jdbc :as jdbc]))

(def options {:pool-name     "db-pool"
              :adapter       "postgresql"
              :username      "minimily_usr"
              :password      "secret"
              :database-name "minimily"
              :server-name   "localhost"
              :port-number   5432})

(def datasource
  (make-datasource options))

(defn resultset [query]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (let [rows (jdbc/query conn query)]
      rows)))
