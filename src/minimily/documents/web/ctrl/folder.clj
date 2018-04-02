(ns minimily.documents.web.ctrl.folder
  (:require [ring.util.response                    :refer [redirect]]
            [minimily.documents.web.ui.folders     :refer [folders-page]]
            [minimily.documents.web.ui.folder-form :refer [folder-form-new 
                                                           folder-form-edit]]
            [minimily.documents.web.ui.folder      :refer [folder-page]]
            [minimily.documents.model.folder       :as folder-model]))

(defn view-parent-folders [session]
  (let [folders (map #(conj % {:num-children (folder-model/count-children (:id %))})
                     (folder-model/find-parents))]
    (folders-page session folders)))

(defn view-folder [session id]
  (let [folder (folder-model/get-it id)
        children (map #(conj % {:num-children (folder-model/count-children (:id %))}) 
                      (folder-model/find-children id))
        path (folder-model/find-path id)]
    (folder-page session folder children path)))

(defn new-folder [session parent-id]
  (let [parent (folder-model/get-it parent-id)]
    (folder-form-new session parent)))

(defn edit-folder [session id]
  (let [folder (folder-model/get-it id)]
    (folder-form-edit session folder)))

(defn save-folder [session folder]
  (let [parent (when (:parent folder) (Integer/parseInt (:parent folder)))
        folder (conj folder {:parent parent})
        id (folder-model/save folder)]
    (redirect (str "/folders/" (if (nil? parent) id parent)))))

(defn delete-folder [params]
  (let [id (:id params)]
    (folder-model/delete-it id)
    (redirect "/folders")))