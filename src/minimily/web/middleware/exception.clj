(ns minimily.web.middleware.exception)

(defn wrap-exception [handler]
  (fn [request]
    (try (handler request)
         (catch Exception e
           (.printStackTrace e)
           {:status 500
            :body (str "Exception caught: " e)}))))