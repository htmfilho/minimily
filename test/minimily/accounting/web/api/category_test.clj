(ns minimily.accounting.web.api.category-test
  (:require [clojure.test :refer                  :all]
            [minimily.accounting.web.api.category :refer :all]))

(deftest test-list-tree-branches
  (testing "The creation of a flat tree based on a list containing hierachy data."
    (is (= [{:id 1 :parent nil}
            {:id 2 :parent {:id 1 :parent nil}}
            {:id 3 :parent {:id 1 :parent nil}}
            {:id 5 :parent {:id 2 :parent {:id 1 :parent nil}}}]
           (list-tree-branches [{:id 1 :parent nil}
                                {:id 2 :parent 1}
                                {:id 3 :parent 1}
                                {:id 5 :parent 2}])))))