(ns minimily.accounting.web.ctrl.category
  (:require [ring.util.response                       :refer [redirect]]
            [minimily.accounting.web.ui.categories    :refer [categories-page]]
            [minimily.accounting.web.ui.category-form :refer [category-form-new 
                                                              category-form-edit]]
            [minimily.accounting.web.ui.category      :refer [category-page]]
            [minimily.accounting.model.category       :as category-model]))

(defn view-parent-categories [session]
  (let [categories (category-model/find-parents (:user-id session))]
    (categories-page session categories)))

(defn view-category [session id]
  (let [profile-id  (:user-id session)
        category-id (Integer/parseInt id)
        category    (category-model/get-it profile-id category-id)
        children    (category-model/find-children profile-id category-id)
        path        (category-model/find-path profile-id category-id)]
    (category-page session category children path)))

(defn new-category [session parent-id]
  (let [parent (category-model/get-it (:user-id session) parent-id)]
    (category-form-new session parent)))

(defn edit-category [session id]
  (let [category (category-model/get-it (:user-id session) id)]
    (category-form-edit session category)))

(defn save-category [session category]
  (println category)
  (let [parent           (when (:parent category) (Integer/parseInt (:parent category)))
        transaction_type (Integer/parseInt (:transaction_type category))
        category         (conj category {:parent parent 
                                         :profile (:user-id session)
                                         :transaction_type transaction_type})
        id (category-model/save category)]
    (redirect (str "/accounting/categories/" (if (nil? parent) id parent)))))

(defn delete-category [session params]
  (let [category-id (Integer/parseInt (:id params))]
    (category-model/delete-it (:user-id session) category-id)
    (redirect "/accounting/categories")))