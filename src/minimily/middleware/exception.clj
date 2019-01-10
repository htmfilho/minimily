(ns minimily.middleware.exception
  (:require [clojure.stacktrace :refer [print-stack-trace]]))

(defn wrap-exception [handler]
  (fn [request]
    (try (handler request)
         (catch Exception e
           (print-stack-trace e)
           {:status 500
            :body (str "Exception caught: " e)}))))