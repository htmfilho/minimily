(ns minimily.accounting.web.ctrl.category
  (:require [ring.util.response                       :refer [redirect]]
            [minimily.accounting.web.ui.categories    :refer [categories-page]]
            [minimily.accounting.web.ui.category-form :refer [category-form-new 
                                                              category-form-edit]]
            [minimily.accounting.web.ui.category      :refer [category-page]]
            [minimily.accounting.model.category       :as category-model]))

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
  (-> categories
      (list-tree-branches)
      (filter-tree-leafs categories)
      (flatten-tree-branch-names)))

(defn view-parent-categories [session]
  (let [categories (category-model/find-parents (:user-id session))]
    (categories-page session categories)))

(defn view-category [session id]
  (let [profile-id  (:user-id session)
        category-id (Integer/parseInt id)
        category    (category-model/get-it profile-id category-id)
        children    (category-model/find-children profile-id category-id)
        path        (category-model/find-path profile-id category-id)]
    (category-page session category children path)))

(defn new-category [session parent-id]
  (let [parent (category-model/get-it (:user-id session) parent-id)]
    (category-form-new session parent)))

(defn edit-category [session id]
  (let [category (category-model/get-it (:user-id session) id)]
    (category-form-edit session category)))

(defn save-category [session category]
  (println category)
  (let [parent           (when (:parent category) (Integer/parseInt (:parent category)))
        transaction_type (Integer/parseInt (:transaction_type category))
        category         (conj category {:parent parent 
                                         :profile (:user-id session)
                                         :transaction_type transaction_type})
        id (category-model/save category)]
    (redirect (str "/accounting/categories/" (if (nil? parent) id parent)))))

(defn delete-category [session params]
  (let [category-id (Integer/parseInt (:id params))]
    (category-model/delete-it (:user-id session) category-id)
    (redirect "/accounting/categories")))