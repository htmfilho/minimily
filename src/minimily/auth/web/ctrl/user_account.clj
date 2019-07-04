(ns minimily.auth.web.ctrl.user-account
  (:require [ring.util.response               :refer [redirect]]
            [minimily.utils.web.wrapper       :refer [http-headers]]
            [minimily.web.ui.layout           :refer [layout]]
            [minimily.auth.model.user-account :as user-account-model]
            [minimily.auth.model.user-profile :as user-profile-model]
            [minimily.auth.web.ui.signin      :refer [signin-content]]
            [minimily.auth.web.ui.password    :as password-ui]))

(defn new-account [params]
  (let [user-account-id (user-account-model/save {:username (:email params)
                                                  :password (:password params)})]
    (user-profile-model/save {:user_account user-account-id
                              :first_name   (:first_name params)
                              :last_name    (:last_name params)
                              :email        (:email params)})
    (redirect "/signin")))

(defn signin-page []
  (http-headers 
    (layout nil "Sign In"
      (signin-content))))

(defn signin [params session]
  (let [auth-user (user-account-model/authenticate (:username params) 
                                                   (:password params))]
    (if auth-user
      (let [session {:full-name (user-profile-model/full-name auth-user)
                     :user-id   (:id auth-user)}
            forward (:forward params)]
        (-> (redirect (if (or (.isEmpty forward) (.equals forward "/signin")) "/" forward))
            (assoc :session session))))))

(defn signin-fail []
  (redirect "/"))

(defn signout [session]
  (-> (redirect "/")
      (assoc :session nil)))

(defn request-reset-password
  ([] (request-reset-password nil))
  ([message] (password-ui/password-reset-request-page message)))

(defn send-request-reset-password [params]
  (let [email         (:email params)
        existing-user (user-account-model/find-by-username email)]
    ; Check if the email exists
    (if (not (empty? existing-user))
      ; Generate a UUID code and associate it with the user
      (let [uuid (user-account-model/generate-uuid)]
        (user-account-model/set-verification (:id existing-user) uuid)
        ; Send a message with the UUID code
        (user-account-model/send-request-reset email uuid)
        (redirect "/account/pswd/reset/request/verify"))
      (request-reset-password "The informed email is unknown. Please, try again below or <a href='/signup'>create a new account</a>."))))

(defn verify-request-reset-password [params]
  (password-ui/password-reset-request-submitted-page params))

(defn check-code-reset-password [params]
  (if-let [existing-user (user-account-model/find-by-verification (:verification params))]
    (do
      (user-account-model/reset-verification (:id existing-user))
      (redirect "/account/pswd/change"))
    )
  ; Check if the informed UUID is associated with a user
  ; If yes, clean up the UUID, create a session for the user as she was authenticated
  ; If not, inform the UUID is invalid and suggest to restart the process again
  )

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