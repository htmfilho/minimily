(ns minimily.accounting.model.account-test
  (:require [clojure.test                      :refer :all]
            [minimily.accounting.model.account :refer :all]))

(deftest test-percentage-used-credit
  (testing "Check division by 0"
    (let [zero (BigDecimal. 0)
          one  (BigDecimal. 1)])
    (is (= (percentage-used-credit {:balance nil :debit_limit nil}) 0))
    (is (= (percentage-used-credit {:balance (BigDecimal. 0) :debit_limit (BigDecimal. 0)}) 0))
    (is (= (percentage-used-credit {:balance (BigDecimal. -1) :debit_limit (BigDecimal. 0)}) 0))
    (is (= (percentage-used-credit {:balance (BigDecimal. 0) :debit_limit (BigDecimal. -1)}) 0))
    (is (= (percentage-used-credit {:balance (BigDecimal. -1) :debit_limit (BigDecimal. -1)}) 0))
    (is (= (percentage-used-credit {:balance (BigDecimal. 50) :debit_limit (BigDecimal. 100)}) 50))
    (is (= (percentage-used-credit {:balance (BigDecimal. 150) :debit_limit (BigDecimal. 100)}) 150))))