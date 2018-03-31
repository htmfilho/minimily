(ns minimily.accounting.web.ctrl.transfer
  (:require [ring.util.response                       :refer [redirect]]
            [minimily.accounting.web.ui.transfer-form :as form]
            [minimily.accounting.web.ui.transaction   :refer [transaction-page]]
            [minimily.accounting.model.account        :as account-model]
            [minimily.accounting.model.transaction    :as transaction-model]))

(defn new-transfer [session account]
  (let [account (account-model/get-it account)
        to-accounts (account-model/find-all-except (:user-id session) 
                                                   (:id account))]
    (form/transfer-form-page session account to-accounts)))

(defn perform-transfer [transaction]
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
