(ns minimily.utils.string-test
  (:require [clojure.test :refer :all]
            [minimily.utils.string :refer :all]))

(deftest test-tech
  (testing "Tests if a string is compatible with low level systems."
    (is (.contains (tech "onça") "c"))
    (is (.equals (tech "&don't") "dont"))
    (is (.equals (tech "àãáâ") "aaaa"))))