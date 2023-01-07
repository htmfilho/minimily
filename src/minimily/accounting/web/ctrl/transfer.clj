(ns minimily.accounting.web.ctrl.transfer
  (:require [ring.util.response                       :refer [redirect]]
            [minimily.utils.date                      :refer [to-date]]
            [minimily.auth.model.user-profile         :as user-profile-model]
            [minimily.accounting.web.ui.transfer-form :as form]
            [minimily.accounting.web.ui.transaction   :refer [transaction-page]]
            [minimily.accounting.web.ui.transfer      :as transfer-view]
            [minimily.accounting.model.account        :as account-model]
            [minimily.accounting.model.transaction    :as transaction-model]
            [minimily.accounting.model.transfer       :as transfer-model]))

(defn new-transfer [session account]
  (let [account (account-model/get-it (:profile-id session) account)
        to-accounts (account-model/find-actives-except (:profile-id session) 
                                                       (:id account))]
    (form/transfer-form-page session account to-accounts)))

(defn perform-transfer-to-account [session params]
  (let [from             (account-model/get-it (:profile-id session) (:account params))
        to               (account-model/get-it (:profile-id session) (:to_account params))
        amount-from      (BigDecimal. (:amount params))
        amount-to        (BigDecimal. (:amount_to params))
        fee              (if (contains? params :fee) (BigDecimal. (:fee params)) (BigDecimal. 0))
        date-transaction (to-date (:date_transaction params) "yyyy-MM-dd")
        transfer         {:profile_from (:profile from)
                          :account_from (:id from)
                          :profile_to (:profile to)
                          :account_to (:id to)
                          :amount amount-to
                          :currency (:currency from)
                          :date_created date-transaction
                          :date_completed date-transaction
                          :description (str "Transfer from " (:name from) " to " (:name to))}
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

    (transfer-model/save transfer)
    
    (redirect (str "/accounting/accounts/" (:id from)))))

(defn perform-transfer-to-user [session params]
  (let [account-from     (account-model/get-it (:profile-id session) (:account params))
        profile-to       (user-profile-model/find-profiles-by-email (:to_user params)) 
        transfer         {:profile_from (:profile-id session)
                          :account_from (:id account-from)
                          :profile_to (:id profile-to)
                          :amount (BigDecimal. (:amount params))
                          :currency (:currency account-from)
                          :date_created (to-date (:date_transaction params) "yyyy-MM-dd")
                          :description (str "Transfer from " (:name account-from))}]
    (transfer-model/save transfer)
    (redirect (str "/accounting/accounts/" (:id account-from)))))

(defn perform-transfer [session transfer]
  (if (empty? (:to_user transfer))
    (perform-transfer-to-account session transfer)
    (perform-transfer-to-user session transfer)))

(defn view-transfers [session]
  (let [transfers-from (transfer-model/find-transfers-from-profile (:profile-id session))
        transfers-to   (transfer-model/find-transfers-to-profile (:profile-id session))]
    (transfer-view/transfers-page session transfers-from transfers-to)))

(defn view-transfer [session id]
  (let [transfer (transfer-model/get-it id)
        accounts (account-model/find-actives (:profile-id session))]
    (transfer-view/transfer-page session transfer accounts)))