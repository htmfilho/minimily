(ns minimily.accounting.web.api.category-test
  (:require [clojure.test :refer                  :all]
            [minimily.accounting.web.api.category :refer :all]))

(deftest test-list-tree-branches
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

(deftest test-filter-tree-leafs
  ; "A single node is parent and leaf at the same time"
  (testing "Leafs from two roots"
    (is (= (filter-tree-leafs [{:id 1 :parent nil}
                               {:id 5 :parent nil}
                               {:id 2 :parent {:id 1 :parent nil}}
                               {:id 3 :parent {:id 1 :parent nil}}
                               {:id 4 :parent {:id 2 :parent {:id 1 :parent nil}}}
                               {:id 6 :parent {:id 5 :parent nil}}
                               {:id 7 :parent {:id 5 :parent nil}}]
                              [{:id 1 :parent nil}
                               {:id 2 :parent 1}
                               {:id 3 :parent 1}
                               {:id 4 :parent 2}
                               {:id 5 :parent nil}
                               {:id 6 :parent 5}
                               {:id 7 :parent 5}])
           [{:id 3 :parent {:id 1 :parent nil}}
            {:id 4 :parent {:id 2 :parent {:id 1 :parent nil}}}
            {:id 6 :parent {:id 5 :parent nil}}
            {:id 7 :parent {:id 5 :parent nil}}]))))

(deftest test-flatten-branch-name
  (testing "Print a branch"
    (is (= (flatten-branch-name {:id 4 :name "three" :parent {:id 2 :name "two" :parent {:id 1 :name "one" :parent nil}}} "/")
           "one/two/three"))))

(deftest test-flatten-tree-branch-names
  (testing "Print branches"
    (is (= (flatten-tree-branch-names [{:id 3 :name "two" :parent {:id 1 :name "one" :parent nil}}
                                       {:id 4 :name "three" :parent {:id 2 :name "two" :parent {:id 1 :name "one" :parent nil}}}
                                       {:id 6 :name "two" :parent {:id 5 :name "one" :parent nil}}
                                       {:id 7 :name "two" :parent {:id 5 :name "one" :parent nil}}])
           [{:id 3 :name "one > two"}
            {:id 4 :name "one > two > three"}
            {:id 6 :name "one > two"}
            {:id 7 :name "one > two"}]))))