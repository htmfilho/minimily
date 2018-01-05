(ns minimily.app
  (:require [clojure.java.io         :as io]
            [ring.middleware.reload  :refer [wrap-reload]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.adapter.jetty      :as jetty]
            [compojure.core          :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler       :as handler]
            [compojure.route         :as route]
            [config.core             :refer [env]]
            [minimily.web.routing    :as routing]
            [minimily.utils.database :as db])
  (:gen-class))

(defonce server (atom nil))

(defn start-server [port]
  (let [routing-app (wrap-session (handler/site #'routing/app))]
    (reset! server (jetty/run-jetty (if (env :reload)
                                      (wrap-reload routing-app)
                                      routing-app) 
                                    {:port port :join? false})))
  (println (str "Go to http://localhost:" port)))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn -main [& [port]]
  (db/migrate)
  (let [port (Integer. (or port (env :port) 5000))]
    (start-server port)))
