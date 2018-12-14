(ns minimily.accounting.web.api.category
  (:require [minimily.accounting.model.category :as category-model]
            [clojure.data.json                  :as json]))

(defn list-tree-branches
  [categories]
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

(defn filter-tree-leafs
  "List of branches where the ids are not in the list of parents of the categories,
   characterizing then as leafs"
  [tree categories]
  (let [parent-ids (set (filter #(some? %) (map #(:parent %) categories)))]
    (filter #(not (contains? parent-ids (:id %))) tree)))

(defn flatten-branch-name
  [branch separator]
  (if (nil? (:parent branch))
    (:name branch)
    (str (flatten-branch-name (:parent branch) separator) separator (:name branch))))

(defn flatten-tree-branch-names
  "Iterates over the branches and create a new list with ids and names only"
  [branches]
  (map #(zipmap [:id :name] [(:id %) (flatten-branch-name % " > ")]) branches))

(defn list-categories [categories]
  (let [branches   (-> categories
                       (list-tree-branches)
                       (filter-tree-leafs categories)
                       (flatten-tree-branch-names))]
    (json/write-str branches)))

(defn list-credit-categories [session]
  (let [categories (category-model/find-credit-categories (:user-id session))]
    (list-categories categories)))

(defn list-debit-categories [session]
  (let [categories (category-model/find-debit-categories (:user-id session))]
    (list-categories categories)))