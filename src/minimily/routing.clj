(ns minimily.routing
  (:require [compojure.core          :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.route         :as route]
            [clojure.java.io         :as io]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Minimily!"})

(defroutes app
  (GET "/" []
       (splash))
  (ANY "*" []
       (route/not-found (slurp (io/resource "public/404.html")))))