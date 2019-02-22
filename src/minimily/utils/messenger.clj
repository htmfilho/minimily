(ns minimily.utils.messenger
  (:require [postal.core :as postal]
            [config.core :refer [env]]))

(defn send [message]
  (postal/send-message {:host (env  :EMAIL_HOST)
                        :user (env  :EMAIL_USER)
                        :pass (env  :EMAIL_PASSWORD)
                        :port (Integer/parseInt (env  :EMAIL_PORT))
                        :tls  true}
                       message))

(defn send-message [to subject body]
  (println body)
  (send {:from (env :EMAIL_FROM)
         :to [to]
         :subject subject
         :body body}))