(ns minimily.auth.middleware.authentication
  (:require [ring.util.response          :refer [redirect]]
            [minimily.auth.web.ui.signin :as signin]
            [minimily.web.ui.layout      :refer [layout]]
            [minimily.utils.web.wrapper  :refer [http-headers]]))

(def public-uris [""
                  "/"
                  "/account/new"
                  "/account/login"
                  "/account/login/fail"
                  "/css/bootstrap.min.css"
                  "/css/bootstrap.min.css.map"
                  "/css/custom.css"
                  "/favicon.ico"
                  "/js/jquery-3.3.1.min.js"
                  "/js/bootstrap.bundle.min.js"
                  "/js/bootstrap.bundle.min.js.map"
                  "/js/echarts.min.js"
                  "/js/minimily.js"
                  "/js/minimily-accounting.js"
                  "/request"
                  "/signin"
                  "/signup"])

(defn is-public-uri? [uri]
  (>= (.indexOf public-uris uri) 0))

(defn is-protected? [uri session]
  (if (is-public-uri? uri)
    false
    (empty? session)))

(defn wrap-authentication [handler]
  (fn [request]
    (let [session (:session request)
          uri     (:uri request)]
      (println (str (when session (:user-id session)) " - " uri))
      (if (is-protected? uri session)
        (redirect (str "/signin?forward=" uri))
        (handler request)))))