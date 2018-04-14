(ns minimily.accounting.web.ctrl.transaction
  (:require [ring.util.response                          :refer [redirect]]
            [minimily.accounting.web.ui.transaction-form :as form]
            [minimily.accounting.web.ui.transaction      :refer [transaction-page]]
            [minimily.accounting.model.account           :as account-model]
            [minimily.accounting.model.transaction       :as transaction-model]))

(defn view-transaction [session account id]
  (let [account     (account-model/get-it (:user-id session) account)
        transaction (transaction-model/get-it (:user-id session) id)]
    (transaction-page session account transaction)))

(defn new-transaction [session account]
  (let [account (account-model/get-it (:user-id session) account)]
    (form/transaction-form-add session account)))

(defn edit-transaction [session account id]
  (form/transaction-form-edit session 
                              (account-model/get-it (:user-id session) account) 
                              (transaction-model/get-it (:user-id session) id)))

(defn add-transaction [session transaction]
  (let [account-id  (Integer/parseInt (:account transaction))
        type        (Integer/parseInt (:type transaction))
        amount      (BigDecimal. (:amount transaction))
        balance     (account-model/update-balance account-id
                                                  (+ (* type amount) 
                                                     (transaction-model/calculate-balance (:user-id session) account-id)))
        transaction (-> transaction
                        (conj {:account account-id})
                        (conj {:type (Integer/parseInt (:type transaction))})
                        (conj {:amount (BigDecimal. (:amount transaction))})
                        (conj {:balance balance}))]
    (transaction-model/save transaction)
    (redirect (str "/accounts/" (:account transaction)))))

(defn save-transaction [session transaction]
  (let [transaction (-> transaction
                        (conj {:account (Integer/parseInt (:account transaction))})
                        (conj {:profile (:user-id session)}))]
    (transaction-model/save transaction)
    (redirect (str "/accounts/" (:account transaction) "/transactions/" (:id transaction)))))

(defn delete-transaction [session params]
  (let [account     (:account params)
        id          (:id params)
        transaction (transaction-model/get-it id)]
    (account-model/update-balance account
                                  (- (transaction-model/calculate-balance (:user-id session) account)
                                     (* (:type transaction) (:amount transaction))))
    (transaction-model/delete-it id)
    (redirect (str "/accounts/" account))))