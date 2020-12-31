(ns minimily.accounting.web.ctrl.transfer
  (:require [ring.util.response                       :refer [redirect]]
            [minimily.utils.date                      :refer [to-date]]
            [minimily.accounting.web.ui.transfer-form :as form]
            [minimily.accounting.web.ui.transaction   :refer [transaction-page]]
            [minimily.accounting.model.account        :as account-model]
            [minimily.accounting.model.transaction    :as transaction-model]))

(defn new-transfer [session account]
  (let [account (account-model/get-it (:profile-id session) account)
        to-accounts (account-model/find-actives-except (:profile-id session) 
                                                       (:id account))]
    (form/transfer-form-page session account to-accounts)))

(defn perform-transfer [session transfer]
  (println transfer)
  (let [from             (account-model/get-it (:profile-id session) (:account transfer))
        to               (account-model/get-it (:profile-id session) (:to transfer))
        amount-from      (BigDecimal. (:amount transfer))
        amount-to        (BigDecimal. (:amount_to transfer))
        fee              (if (contains? transfer :fee) (BigDecimal. (:fee transfer)) (BigDecimal. 0))
        date-transaction (to-date (str (:date_transaction transfer) "12:00:00 -0500") "yyyy-MM-dd HH:mm:ss Z")
        transaction-from {:account (:id from) 
                          :type -1
                          :amount (.add amount-from fee)
                          :description (str "Transfer to " (:name to))
                          :account_transfer (:id to)
                          :date_transaction date-transaction
                          :profile (:profile-id session)}
        transaction-to   {:account (:id to) 
                          :type 1
                          :amount amount-to 
                          :description (str "Transfer from " (:name from))
                          :account_transfer (:id from)
                          :date_transaction date-transaction
                          :profile (:profile-id session)}]
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
    
    (redirect (str "/accounting/accounts/" (:id from)))))
