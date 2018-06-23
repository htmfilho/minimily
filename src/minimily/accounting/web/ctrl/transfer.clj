(ns minimily.accounting.web.ctrl.transfer
  (:require [ring.util.response                       :refer [redirect]]
            [minimily.utils.date                      :refer [to-timestamp]]
            [minimily.accounting.web.ui.transfer-form :as form]
            [minimily.accounting.web.ui.transaction   :refer [transaction-page]]
            [minimily.accounting.model.account        :as account-model]
            [minimily.accounting.model.transaction    :as transaction-model]))

(defn new-transfer [session account]
  (let [account (account-model/get-it (:user-id session) account)
        to-accounts (account-model/find-all-except (:user-id session) 
                                                   (:id account))]
    (form/transfer-form-page session account to-accounts)))

(defn perform-transfer [session transfer]
  (let [from             (account-model/get-it (:user-id session) (:account transfer))
        to               (account-model/get-it (:user-id session) (:to transfer))
        amount           (BigDecimal. (:amount transfer))
        date-transaction (to-timestamp (:date_transaction transfer) "yyyy-MM-dd")
        transaction-from {:account (:id from) 
                          :type -1
                          :amount amount
                          :description (str "Transfer to " (:name to))
                          :account_transfer (:id to)
                          :date_transaction date-transaction
                          :profile (:user-id session)}
        transaction-to   {:account (:id to) 
                          :type 1
                          :amount amount 
                          :description (str "Transfer from " (:name from))
                          :account_transfer (:id from)
                          :date_transaction date-transaction
                          :profile (:user-id session)}]
    
    (account-model/update-balance (:account transaction-from)
                                  (+ (* (:type transaction-from) 
                                        (:amount transaction-from))
                                     (transaction-model/calculate-balance (:account transaction-from))))
    (transaction-model/save transaction-from)
    
    (account-model/update-balance (:account transaction-to)
                                  (+ (* (:type transaction-to)
                                        (:amount transaction-to))
                                     (transaction-model/calculate-balance (:account transaction-to))))
    (transaction-model/save transaction-to)
    
    (redirect (str "/accounts/" (:id from)))))
