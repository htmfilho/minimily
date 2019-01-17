(ns minimily.accounting.web.api.category
  (:require [minimily.accounting.model.category    :as category-model]
            [minimily.accounting.web.ctrl.category :as category-ctrl]
            [clojure.data.json                     :as json]))

(defn list-credit-categories [session]
  (let [categories (category-model/find-credit-categories (:user-id session))]
    (json/write-str (category-ctrl/list-categories categories))))

(defn list-debit-categories [session]
  (let [categories (category-model/find-debit-categories (:user-id session))]
    (json/write-str (category-ctrl/list-categories categories))))