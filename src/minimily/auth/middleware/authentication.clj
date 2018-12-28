(ns minimily.auth.middleware.authentication
  (:require [minimily.auth.web.ui.signin :as signin]
            [minimily.web.ui.layout      :refer [layout]]
            [minimily.utils.web.wrapper  :refer [http-headers]]))

(def public-pages [""
                   "/"
                   "/account/new"
                   "/account/login"
                   "/account/login/fail"
                   "/request"
                   "/signin"
                   "/signup"])

(defn is-protected? [uri session]
  (if (>= (.indexOf public-pages uri) 0)
    false
    (empty? session)))

(defn wrap-authentication [handler]
  (fn [request]
    (let [session (:session request)
          uri     (:uri request)]
      (println uri)
      (if (is-protected? uri session)
        (http-headers
          (layout session "Sign In"
            (signin/signin-content)))
        (handler request)))))