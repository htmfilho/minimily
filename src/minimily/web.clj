(ns minimily.web
  (:require [compojure.core          :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler       :refer [site]]
            [compojure.route         :as route]
            [clojure.java.io         :as io]
            [ring.adapter.jetty      :as jetty]
            [config.core             :refer [env]]
            [minimily.utils.database :refer :all]
            [ragtime.jdbc            :as migration]
            [ragtime.repl            :as repl])
  (:gen-class))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Minimily"})

(defroutes app
  (GET "/" []
       (splash))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(def migration-config
 {:datastore  (migration/sql-database {:datasource datasource})
  :migrations (migration/load-resources "migrations")})

(defn -main [& [port]]
  (repl/migrate migration-config)
  (println (find-records "SELECT 0"))
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
