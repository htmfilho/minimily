(ns minimily.accounting.web.api.category-test
  (:require [clojure.test :refer                  :all]
            [minimily.accounting.web.api.category :refer :all]))

(deftest test-list-tree-branches
  ; The creation of a flat tree based on a list containing hierachy data.
  (testing "Empty category"
    (is (= (list-tree-branches []) [])))

  (testing "Only parents"
    (is (= (list-tree-branches [{:id 1 :parent nil}
                                {:id 2 :parent nil}])
           [{:id 1 :parent nil}
            {:id 2 :parent nil}])))

  (testing "Only one branch"
    (is (= (list-tree-branches [{:id 1 :parent nil}
                                {:id 2 :parent 1}])
           [{:id 1 :parent nil}
            {:id 2 :parent {:id 1 :parent nil}}])))
    
  (testing "A tree of height 3."
    (is (= (list-tree-branches [{:id 1 :parent nil}
                                {:id 2 :parent 1}
                                {:id 3 :parent 1}
                                {:id 4 :parent 2}])
           [{:id 1 :parent nil}
            {:id 2 :parent {:id 1 :parent nil}}
            {:id 3 :parent {:id 1 :parent nil}}
            {:id 4 :parent {:id 2 :parent {:id 1 :parent nil}}}])))
  
  (testing "2 trees."
    (is (= (list-tree-branches [{:id 1 :parent nil}
                                {:id 2 :parent 1}
                                {:id 3 :parent 1}
                                {:id 4 :parent 2}
                                {:id 5 :parent nil}
                                {:id 6 :parent 5}
                                {:id 7 :parent 5}])
           [{:id 1 :parent nil}
            {:id 5 :parent nil}
            {:id 2 :parent {:id 1 :parent nil}}
            {:id 3 :parent {:id 1 :parent nil}}
            {:id 4 :parent {:id 2 :parent {:id 1 :parent nil}}}
            {:id 6 :parent {:id 5 :parent nil}}
            {:id 7 :parent {:id 5 :parent nil}}]))))