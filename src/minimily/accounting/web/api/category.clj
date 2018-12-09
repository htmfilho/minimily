(ns minimily.accounting.web.api.category
  (:require [minimily.accounting.model.category :as category-model]
            [clojure.data.json                  :as json]))

(defn list-tree-branches [categories]
  (loop [categories categories
         remaining  categories
         tree []]
    (if (empty? remaining)
      tree
      (if (empty? tree)
        ; Initialize the tree with the parents.
        (let [parents    (filter #(nil? (:parent %)) categories)
              remaining  (filter #(some? (:parent %)) categories)]
          (recur remaining 
                 remaining 
                 parents)) ; the tree is initialized with the parents.
        ; Deals with the branches.
        (let [category (first categories)
              parent   (first (filter #(= (:id %) (:parent category)) tree))]
          (if (nil? parent)
            ; It didn't find the parent yet, so it keeps looking for the 
            ; categories in the tree.
            (recur (rest categories) 
                   remaining
                   tree)
            ; If the parent is in the tree, replace the parent of the 
            ; category with the parent found, add the category to the tree, and 
            ; remove the category from the remaining.
            (let [node      (conj category {:parent parent})
                  remaining (filter #(not= (:id %) (:id category)) remaining)]
              (recur remaining
                     remaining
                     (conj (vec tree) node)))))))))

(defn list-credit-categories [session]
  (json/write-str (category-model/find-credit-categories (:user-id session))))

(defn list-debit-categories [session]
  (let [categories (category-model/find-debit-categories (:user-id session))
        tree       (list-tree-branches categories)]
    (json/write-str categories)))
