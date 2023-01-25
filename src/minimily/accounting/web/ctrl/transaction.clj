(ns minimily.accounting.web.ctrl.transaction
  (:require [ring.util.response                     :refer [redirect]]
            [minimily.utils.date                    :refer [to-date]]
            [minimily.accounting.web.ui.transaction :as transaction-view]
            [minimily.accounting.web.ctrl.category  :as category-ctrl]
            [minimily.accounting.model.account      :as account-model]
            [minimily.accounting.model.transaction  :as transaction-model]
            [minimily.accounting.model.transfer     :as transfer-model]
            [minimily.accounting.model.category     :as category-model]
            [minimily.accounting.model.third-party  :as third-party-model]))

(defn view-transaction [session account id]
  (let [account     (account-model/get-it account (:profile-id session))
        transaction (transaction-model/get-it (:profile-id session) id)
        category    (category-model/get-it (:profile-id session) (:category transaction))]
    (transaction-view/transaction-page session account transaction category)))

(defn new-transaction [session account]
  (let [account       (account-model/get-it account (:profile-id session))
        third-parties (third-party-model/find-other-third-parties (:id account) (:profile-id session))]
    (transaction-view/transaction-form-add session account third-parties)))

(defn edit-transaction [session account id]
  (let [profile-id  (:profile-id session)
        transaction (transaction-model/get-it profile-id id)
        categories  (if (= (:type transaction) transaction-model/DEBIT)
                                  (category-model/find-debit-categories profile-id)
                                  (category-model/find-credit-categories profile-id))]
    (transaction-view/transaction-form-edit session 
                                            (account-model/get-it account (:profile-id session)) 
                                            transaction
                                            (map #(if (= (:id %) (:category transaction))
                                                    (conj % {:selected true})
                                                    (conj % {:selected false})) 
                                                (category-ctrl/list-categories categories)))))

(defn params-to-transaction [session params]
  (-> params
      (dissoc :third_party_account)
      (dissoc :third_party)
      (conj {:account (Integer/parseInt (:account params))})
      (conj {:category (if (empty? (:category params)) 
                         nil
                         (Integer/parseInt (:category params)))})
      (conj {:type (Integer/parseInt (:type params))})
      (conj {:amount (BigDecimal. (:amount params))})
      (conj {:date_transaction (to-date (:date_transaction params) "yyyy-MM-dd")})
      (conj {:profile (:profile-id session)})))

(defn add-transaction [session params]
  (let [third-party-account (Integer/parseInt (:third_party_account params))
        transaction-to      (conj (params-to-transaction session params)
                                  {:account_transfer third-party-account})
        transaction-from    (transaction-model/create-counter-transaction transaction-to third-party-account)]
    (transfer-model/transfer transaction-from transaction-to)
    (redirect (str "/accounting/accounts/" (:account transaction-to)))))

(defn add-and-new-transaction [session params]
  (let [third-party-account (Integer/parseInt (:third_party_account params))
        transaction-to      (conj (params-to-transaction session params)
                                  {:account_transfer third-party-account})
        transaction-from    (transaction-model/create-counter-transaction transaction-to third-party-account)]
    (transfer-model/transfer transaction-from transaction-to)
    (redirect (str "/accounting/accounts/" (:account transaction-to) "/transactions/new"))))

(defn save-transaction [session params]
  (let [transaction (-> params
                        (conj {:account (Integer/parseInt (:account params))})
                        (conj {:category (if (empty? (:category params)) 
                                          nil
                                          (Integer/parseInt (:category params)))})
                        (conj {:date_transaction (to-date (:date_transaction params) "yyyy-MM-dd")})
                        (conj {:profile (:profile-id session)}))]
    (transaction-model/save transaction)
    (redirect (str "/accounting/accounts/" (:account transaction) "/transactions/" (:id transaction)))))

(defn delete-transaction [session params]
  (let [account-id  (Integer/parseInt (:account params))
        id          (Integer/parseInt (:id params))
        transaction (transaction-model/get-it (:profile-id session) id)]
    (account-model/update-balance account-id
                                  (- (transaction-model/calculate-balance account-id)
                                     (* (:type transaction) (:amount transaction))))
    (transaction-model/delete-it (:profile-id session) id)
    (redirect (str "/accounting/accounts/" account-id))))