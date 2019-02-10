(ns minimily.auth.web.ctrl.user-account
  (:require [ring.util.response               :refer [redirect]]
            [minimily.auth.model.user-account :as user-account-model]
            [minimily.auth.web.ui.password    :as password-ui]))

(defn request-reset-password []
  (password-ui/password-reset-request-page))

(defn send-request-reset-password [params]
  (redirect "/account/pswd/reset/request/verify"))

(defn verify-request-reset-password [params]
  (password-ui/password-reset-request-submitted-page params))

(defn check-code-reset-password [params]
  (redirect "/account/pswd/change"))

(defn changing-password [session]
  (password-ui/password-change-page session))

(defn change-password [params session]
  (redirect "/account/pswd/change/confirmation"))

(defn confirm-change-password []
  (password-ui/password-change-confirmed-page))