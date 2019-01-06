(ns minimily.app
  (:require [ring.middleware.reload                  :refer [wrap-reload]]
            [ring.middleware.session                 :refer [wrap-session]]
            [minimily.middleware.exception           :refer [wrap-exception]]
            [minimily.auth.middleware.authentication :refer [wrap-authentication]]
            [ring.adapter.jetty                      :as jetty]
            [compojure.core                          :refer :all]
            [compojure.handler                       :as handler]
            [config.core                             :refer [env]]
            [minimily.web.routing                    :as routing]
            [minimily.utils.database                 :as db])
  (:gen-class))

(defonce server (atom nil))

(defn start-server
  "2. Start the server at the designated port and associate the routing, wrap 
      the session and the auto-reload."
  [port]
  (let [routing-app (-> (handler/site #'routing/app)
                        wrap-session
                        wrap-authentication
                        wrap-exception)]
    (reset! server (jetty/run-jetty (if (env :reload)
                                      (wrap-reload routing-app)
                                      routing-app) 
                                    {:port port :join? false})))
  (println (str "Go to http://localhost:" port)))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn -main
  "1. Entry point that migrates the database and start the server at port 5000."
  [& [port]]
  (db/migrate)
  (let [port (or port (Integer. (env :port)) 5000)]
    (start-server port)))