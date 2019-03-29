(ns minimily.utils.messenger
  (:require [postal.core :as postal]
            [config.core :refer [env]]))

(defn post [message]
  (postal/send-message {:host (env  :EMAIL_HOST)
                        :user (env  :EMAIL_USER)
                        :pass (env  :EMAIL_PASSWORD)
                        :port (Integer/parseInt (env  :EMAIL_PORT))
                        :tls  true}
                       message))

(defn send-message [to subject body]
  (post {:from (env :EMAIL_FROM)
         :to [to]
         :subject subject
         :body body}))