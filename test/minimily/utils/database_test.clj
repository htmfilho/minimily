(ns minimily.utils.database-test
  (:require [clojure.test :refer :all]
            [minimily.utils.database :refer :all]))

(deftest test-mandatory-datasource-options
  (testing "The mandatory datasource options are present."
    (is (contains? options :adapter))
    (is (contains? options :server-name))
    (is (contains? options :port-number))
    (is (contains? options :database-name))
    (is (contains? options :username))
    (is (contains? options :password))))
