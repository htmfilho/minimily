(ns minimily.web
  (:require [compojure.core          :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler       :refer [site]]
            [compojure.route         :as route]
            [clojure.java.io         :as io]
            [ring.middleware.reload  :refer [wrap-reload]]
            [ring.adapter.jetty      :as jetty]
            [config.core             :refer [env]]
            [minimily.routing        :as routing]
            [minimily.utils.database :as db])
  (:gen-class))

(defonce server (atom nil))

(defn start-server [port]
  (reset! server (jetty/run-jetty (if (env :reload) 
                                    (wrap-reload (site #'routing/app))
                                    (site #'routing/app)) 
                                  {:port port :join? false})))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn -main [& [port]]
  (db/migrate)
  (let [port (Integer. (or port (env :port) 5000))]
    (start-server port)))
