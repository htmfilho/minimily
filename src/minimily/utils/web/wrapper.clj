(ns minimily.utils.web.wrapper)

(defn http-headers [body]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    body})