(ns minimily.utils.database-test
  (:require [clojure.test :refer :all]
            [minimily.utils.database :refer :all]))

(deftest test-mandatory-datasource-options
  (testing "The mandatory datasource options are present."
    (is (reduce #(and %1 %2)
                (map #(contains? options %)
                     [:pool-name :adapter])))))

(deftest test-find-records
  (testing "The find function is operational with a query."
    (is (= (find-records ["select id from account"])))))

(deftest test-delete-record
  (testing "Delete record that doesn't exist"
    (is (= 0 (first (delete-record :account 0))))))