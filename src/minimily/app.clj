(ns minimily.app
  (:require [ring.middleware.reload  :refer [wrap-reload]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.adapter.jetty      :as jetty]
            [compojure.core          :refer :all]
            [compojure.handler       :as handler]
            [config.core             :refer [env]]
            [minimily.web.routing    :as routing]
            [minimily.utils.database :as db])
  (:gen-class))

(defonce server (atom nil))

(defn start-server [port]
  (let [routing-app (-> (handler/site #'routing/app)
                        wrap-session)]
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
