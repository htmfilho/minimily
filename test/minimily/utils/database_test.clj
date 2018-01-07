(ns minimily.utils.database-test
  (:require [clojure.test :refer :all]
            [minimily.utils.database :refer :all]))

(deftest test-mandatory-datasource-options
  (testing "The mandatory datasource options are present."
    (is (reduce #(and %1 %2)
                (map #(contains? options %)
                     [:pool-name :adapter])))))

