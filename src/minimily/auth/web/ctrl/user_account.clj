(ns minimily.auth.web.ctrl.user-account
  (:require [clojure.string                   :as string]
            [ring.util.response               :refer [redirect]]
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

(defn create-session [user]
  {:full-name (user-profile-model/full-name user)
   :user-id   (:id user)})

(defn signin-page
  ([] (signin-page nil))
  ([message]
    (http-headers
      (layout nil "Sign In"
        (signin-content message)))))

(defn signin [params]
  (let [auth-user (user-account-model/authenticate (:username params) 
                                                   (:password params))]
    (if auth-user
      (let [session (create-session auth-user)
            forward (:forward params)]
        (-> (redirect (if (or (.isEmpty forward) (.equals forward "/signin")) "/" forward))
            (assoc :session session)))
      (signin-page "Your credentials don't match. Please, try again."))))

(defn signin-fail []
  (redirect "/"))

(defn signout [session]
  (-> (redirect "/")
      (assoc :session nil)))

(defn request-reset-password
  ([] (request-reset-password nil))
  ([message] (password-ui/password-reset-request-page message)))

(defn send-request-reset-password [params]
  (let [email         (string/trim (:email params))
        existing-user (user-account-model/find-by-username email)]
    ; Check if the email exists
    (if (empty? existing-user)
      (request-reset-password "The informed email is unknown. Please, try again or <a href='/signup'>create a new account</a>.")
      ; Generate a UUID code and associate it with the user
      (let [uuid (user-account-model/generate-uuid)]
        (user-account-model/set-verification (:id existing-user) uuid)
        ; Send a message with the UUID code
        (user-account-model/send-request-reset email uuid)
        (redirect "/account/pswd/reset/request/verify")))))

(defn check-code-reset-password [params]
  (let [verified-user (user-account-model/find-by-verification (string/trim (:recovery-code params)))]
    (if (empty? verified-user)
      (password-ui/password-reset-request-submitted-page params "The informed code is not valid. Try again or <a href='/account/pswd/reset/request'>request a new code</a>.")
      (do
        (user-account-model/reset-verification (:id verified-user))
        (-> (redirect "/account/pswd/change")
            (assoc :session (create-session verified-user)))))))

(defn verify-request-reset-password [params]
  (if (:cd params)
    (check-code-reset-password {:verification (:cd params)})
    (password-ui/password-reset-request-submitted-page params nil)))

(defn changing-password [session]
  (password-ui/password-change-page session nil))

(defn change-password [params session]
  (let [password (:password params)]
    (if (= password (:password_confirm params))
      (do
        (user-account-model/set-new-password (:user-id session) password)
        (redirect "/account/pswd/change/confirmation"))
      (password-ui/password-change-page session "The password and its confirmation don't match. Please, try again."))))

(defn confirm-change-password [session]
  (password-ui/password-change-confirmed-page session))