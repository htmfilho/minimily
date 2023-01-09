(ns minimily.accounting.web.ctrl.third-party
  (:require [ring.util.response                          :refer [redirect]]
            [minimily.accounting.web.ui.third-parties    :refer [third-parties-page]]
            [minimily.accounting.web.ui.third-party      :refer [third-party-page]]
            [minimily.accounting.web.ui.third-party-form :refer [third-party-form-page]]
            [minimily.accounting.model.third-party       :as third-party-model]
            [minimily.accounting.model.account           :as account-model]))

(defn view-third-parties [session]
  (let [third-parties (third-party-model/find-third-parties (:profile-id session))]
    (third-parties-page session third-parties)))

(defn view-third-party [session id]
  (let [third-party (third-party-model/get-it id)
        accounts    (account-model/find-third-party-accounts (:profile-id session) id)]
    (third-party-page session third-party accounts)))

(defn new-third-party [session]
  (third-party-form-page session))

(defn edit-third-party [session id]
  (let [third-party    (third-party-model/get-it id)]
    (third-party-form-page session third-party)))

(defn save-third-party [session params]
  (let [third-party params
        id          (third-party-model/save third-party)]
    (redirect (str "/accounting/third_parties"))))

(defn delete-third-party [session params]
  (let [id (Integer/parseInt (:id params))]
    (third-party-model/delete-it id)
    (redirect "/accounting/third_parties")))