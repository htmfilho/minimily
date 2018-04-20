(ns minimily.accounting.web.ctrl.transfer
  (:require [ring.util.response                       :refer [redirect]]
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
        balance-from     (account-model/update-balance (:id from)
                                                       (+ (* -1 amount)
                                                          (transaction-model/calculate-balance (:id from))))
        balance-to       (account-model/update-balance (:id to)
                                                       (+ amount
                                                          (transaction-model/calculate-balance (:id to))))
        transaction-from {:account (:id from) 
                          :type -1 
                          :amount amount 
                          :description (str "Transfer to " (:name to))
                          :balance balance-from
                          :account_transfer (:id to)
                          :profile (:user-id session)}
        transaction-to   {:account (:id to) 
                          :type 1 
                          :amount amount 
                          :description (str "Transfer from " (:name from))
                          :balance balance-to
                          :account_transfer (:id from)
                          :profile (:user-id session)}]
    (transaction-model/save transaction-from)
    (transaction-model/save transaction-to)
    (redirect (str "/accounts/" (:id from)))))
