(ns minimily.accounting.web.ctrl.transaction
  (:require [ring.util.response                          :refer [redirect]]
            [minimily.accounting.web.ui.transaction-form :refer [transaction-form-page]]
            [minimily.accounting.web.ui.transaction      :refer [transaction-page]]
            [minimily.accounting.model.account           :as account-model]
            [minimily.accounting.model.transaction       :as transaction-model]))

(defn view-transaction [account id]
  (let [account     (account-model/get-it account)
        transaction (transaction-model/get-it id)]
    (transaction-page account transaction)))

(defn new-transaction [session account-id]
  (let [account (account-model/get-it account-id)]
    (transaction-form-page session account)))

(defn edit-transaction [session id]
  (let [transaction (transaction-model/get-it id)]
    (transaction-form-page session transaction)))

(defn save-transaction [transaction]
  (let [account-id  (Integer/parseInt (:account transaction))
        type        (Integer/parseInt (:type transaction))
        amount      (BigDecimal. (:amount transaction))
        balance     (account-model/update-balance account-id
                                                  (+ (* type amount) 
                                                     (transaction-model/calculate-balance account-id)))
        transaction (-> transaction
                        (conj {:account account-id})
                        (conj {:type (Integer/parseInt (:type transaction))})
                        (conj {:amount (BigDecimal. (:amount transaction))})
                        (conj {:balance balance}))]
    (transaction-model/save transaction)
    (redirect (str "/accounts/" (:account transaction)))))

(defn delete-transaction [params]
  (let [account     (:account params)
        id          (:id params)
        transaction (transaction-model/get-it id)]
    (account-model/update-balance account
                                  (- (transaction-model/calculate-balance account)
                                     (* (:type transaction) (:amount transaction))))
    (transaction-model/delete-it id)
    (redirect (str "/accounts/" account))))