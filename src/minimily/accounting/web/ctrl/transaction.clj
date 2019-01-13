(ns minimily.accounting.web.ctrl.transaction
  (:require [ring.util.response                          :refer [redirect]]
            [minimily.utils.date                         :refer [to-date]]
            [minimily.accounting.web.ui.transaction-form :as form]
            [minimily.accounting.web.ui.transaction      :refer [transaction-page]]
            [minimily.accounting.model.account           :as account-model]
            [minimily.accounting.model.transaction       :as transaction-model]
            [minimily.accounting.model.category          :as category-model]))

(defn view-transaction [session account id]
  (let [account     (account-model/get-it (:user-id session) account)
        transaction (transaction-model/get-it (:user-id session) id)
        category    (category-model/get-it (:user-id session) (:category transaction))]
    (transaction-page session 
                      account 
                      (conj transaction {:category {:id   (:id category)
                                                    :name (:name category)}}))))

(defn new-transaction [session account]
  (let [account (account-model/get-it (:user-id session) account)]
    (form/transaction-form-add session account)))

(defn edit-transaction [session account id]
  (let [profile-id  (:user-id session)
        transaction (transaction-model/get-it profile-id id)
        categories  (if (= (:type transaction) transaction-model/DEBIT)
                                  (category-model/find-debit-categories profile-id)
                                  (category-model/find-credit-categories profile-id))]
    (form/transaction-form-edit session 
                                (account-model/get-it (:user-id session) account) 
                                transaction
                                (map #(if (= (:id %) (:category transaction))
                                        (conj % {:selected true})
                                        (conj % {:selected false})) categories))))

(defn create-transaction [session params]
  (-> params
      (conj {:account (Integer/parseInt (:account params))})
      (conj {:category (Integer/parseInt (:category params))})
      (conj {:type (Integer/parseInt (:type params))})
      (conj {:amount (BigDecimal. (:amount params))})
      (conj {:date_transaction (to-date (:date_transaction params) "yyyy-MM-dd")})
      (conj {:profile (:user-id session)})))

(defn add-transaction [session params]
  (let [transaction (create-transaction session params)]
    (transaction-model/add transaction)
    (redirect (str "/accounting/accounts/" (:account transaction)))))

(defn add-and-new-transaction [session params]
  (let [transaction (create-transaction session params)]
    (transaction-model/add transaction)
    (redirect (str "/accounting/accounts/" (:account transaction) "/transactions/new"))))

(defn save-transaction [session params]
  (let [transaction (-> params
                        (conj {:account (Integer/parseInt (:account params))})
                        (conj {:category (Integer/parseInt (:category params))})
                        (conj {:date_transaction (to-date (:date_transaction params) "yyyy-MM-dd")})
                        (conj {:profile (:user-id session)}))]
    (transaction-model/save transaction)
    (redirect (str "/accounting/accounts/" (:account transaction) "/transactions/" (:id transaction)))))

(defn delete-transaction [session params]
  (let [account-id  (Integer/parseInt (:account params))
        id          (Integer/parseInt (:id params))
        transaction (transaction-model/get-it (:user-id session) id)]
    (account-model/update-balance account-id
                                  (- (transaction-model/calculate-balance account-id)
                                     (* (:type transaction) (:amount transaction))))
    (transaction-model/delete-it (:user-id session) id)
    (redirect (str "/accounting/accounts/" account-id))))