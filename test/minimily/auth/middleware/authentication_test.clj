(ns minimily.auth.middleware.authentication-test
  (:require [clojure.test                            :refer :all]
            [minimily.auth.middleware.authentication :refer :all]))

(deftest test-is-protected?
  (testing "Check if a page is protected"
    (is (= (is-protected? "" {})                 false))
    (is (= (is-protected? "/signin" {})          false))
    (is (= (is-protected? "/accounting" {})      true))
    (is (= (is-protected? "/accounting/accounts" 
                          {:user-id "user"})     false))))