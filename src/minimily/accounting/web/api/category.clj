(ns minimily.accounting.web.api.category
  (:require [minimily.accounting.model.category :as category-model]
            [clojure.data.json                  :as json]))

(defn list-tree-branches [categories]
  (println categories)
  (loop [categories categories
         tree []]
    (if (empty? categories)
      tree
      (let [category    (first categories)
            parent      (first (filter #(= (:parent category) (:id %)) tree))
            with-parent (if (empty? parent) nil (conj category {:parent parent}))]
        (recur (rest categories)
               (conj tree with-parent))))))

(defn list-credit-categories [session]
  (json/write-str (category-model/find-credit-categories (:user-id session))))

(defn list-debit-categories [session]
  (json/write-str (category-model/find-debit-categories (:user-id session))))
