(ns minimily.auth.web.ctrl.user-account
  (:require [ring.util.response               :refer [redirect]]
            [minimily.auth.model.user-account :as user-account-model]
            [minimily.auth.web.ui.password    :as password-ui]))

(defn request-reset-password
  ([] (request-reset-password nil))
  ([message] (password-ui/password-reset-request-page message)))

(defn send-request-reset-password [params]
  (let [email (:email params)]
    ; Check if the email exists
    (if (user-account-model/username-exists email)
      (redirect "/account/pswd/reset/request/verify")
      (request-reset-password "The informed email is unknown. Please, try again below or <a href='/signup'>create a new account</a>.")))
  
  ; Generate a UUID code and associate it with the user
  ; Send a message with the UUID code
  )

(defn verify-request-reset-password [params]
  (password-ui/password-reset-request-submitted-page params))

(defn check-code-reset-password [params]
  ; Check if the informed UUID is associated with a user
  ; If yes, clean up the UUID, create a session for the user as she was authenticated
  ; If not, inform the UUID is invalid and suggest to restart the process again
  (redirect "/account/pswd/change"))

(defn changing-password [session]
  ; Load user data to show in the UI
  (password-ui/password-change-page session))

(defn change-password [params session]
  ; Check if password and confirmation are the same and if password respects password policy
  ; If yes, encrypt the password, store it in the database and destroy the 
  ; session to force the user to login again using the new password
  ; If not, ask the user to inform the same password in both fields.
  (redirect "/account/pswd/change/confirmation"))

(defn confirm-change-password []
  (password-ui/password-change-confirmed-page))