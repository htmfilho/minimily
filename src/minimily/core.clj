(ns minimily.core
  (:require [minimily.utils.database :refer :all]
            [clojure.java.jdbc       :as jdbc]
            [ragtime.jdbc            :as migration]
            [ragtime.repl            :as repl]))

(def migration-config
  {:datastore  (migration/sql-database {:datasource datasource})
   :migrations (migration/load-resources "migrations")})

(defn -main [& args]
  (repl/migrate migration-config)
  (println (resultset "SELECT 0")))
