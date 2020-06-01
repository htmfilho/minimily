(ns minimily.app
  (:require [ring.middleware.reload        :refer [wrap-reload]]
            [ring.middleware.session       :refer [wrap-session]]
            [minimily.middleware.exception :refer [wrap-exception]]
            [org.httpkit.server            :refer [run-server]]
            [compojure.core                :refer :all]
            [compojure.handler             :as handler]
            [config.core                   :refer [env]]
            [selmer.parser                 :as parser]
            [minimily.web.routing          :as routing]
            [minimily.utils.database       :as db])
  (:gen-class))

(parser/set-resource-path! (clojure.java.io/resource "templates"))

(defonce server (atom nil))

(defn start-server
  "2. Start the server at the designated port and associate the routing, wrap 
      the session and the auto-reload."
  [port]
  (let [routing-app (-> (handler/site #'routing/app)
                        wrap-exception
                        wrap-session)]
    (reset! server (run-server (if (:reload env)
                                   (wrap-reload routing-app)
                                   routing-app) 
                               {:port port :join? false})))
  (println (str "Go to http://localhost:" port)))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))

(defn -main
  "1. Entry point that migrates the database and start the server at port 5000."
  [& [port]]
  (db/migrate)
  (let [port (or port (Integer. (:port env)) 5000)]
    (start-server port)))