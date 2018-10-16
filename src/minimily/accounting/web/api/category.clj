(ns minimily.accounting.web.api.category
  (:require [minimily.accounting.model.category :as category-model]
            [clojure.data.json                  :as json]))

(defn list-credit-categories [session]
  (json/write-str (category-model/find-credit-categories (:user-id session))))

(defn list-debit-categories [session]
  (json/write-str (category-model/find-debit-categories (:user-id session))))