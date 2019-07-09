(ns minimily.utils.messenger
  (:require [postal.core :as postal]
            [config.core :refer [env]]))

(defn post [message]
  (postal/send-message {:host (or (:EMAIL_HOST env) (System/getenv "EMAIL_HOST"))
                        :user (or (:EMAIL_USER env) (System/getenv "EMAIL_USER"))
                        :pass (or (:EMAIL_PASSWORD env) (System/getenv "EMAIL_PASSWORD"))
                        :port (Integer/parseInt (or (:EMAIL_PORT env) (System/getenv "EMAIL_PORT")))
                        :tls  true}
                       message))

(defn send-message [to subject body]
  (post {:from (or (:EMAIL_FROM env) (System/getenv "EMAIL_FROM"))
         :to [to]
         :subject subject
         :body body}))