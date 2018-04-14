(ns minimily.documents.web.ctrl.folder
  (:require [ring.util.response                    :refer [redirect]]
            [minimily.documents.web.ui.folders     :refer [folders-page]]
            [minimily.documents.web.ui.folder-form :refer [folder-form-new 
                                                           folder-form-edit]]
            [minimily.documents.web.ui.folder      :refer [folder-page]]
            [minimily.documents.model.folder       :as folder-model]
            [minimily.documents.model.document     :as document-model]))

(defn merge-num-children [profile-id folders]
  (map #(conj % {:num-children (document-model/count-siblings profile-id (:id %))})
       folders))

(defn merge-documents [profile-id folder-id folders]
  (let [documents (document-model/find-by-folder profile-id folder-id)]
    (reduce conj documents folders)))

(defn view-parent-folders [session]
  (let [folders (merge-num-children (:user-id session) 
                                    (folder-model/find-parents (:user-id session)))]
    (folders-page session folders)))

(defn view-folder [session id]
  (let [folder (folder-model/get-it (:user-id session) id)
        children (merge-documents (:user-id session) 
                                  id 
                                  (merge-num-children (:user-id session) 
                                                      (folder-model/find-children (:user-id session) id)))
        path (folder-model/find-path (:user-id session) id)]
    (folder-page session folder children path)))

(defn new-folder [session parent-id]
  (let [parent (folder-model/get-it (:user-id session) parent-id)]
    (folder-form-new session parent)))

(defn edit-folder [session id]
  (let [folder (folder-model/get-it (:user-id session) id)]
    (folder-form-edit session folder)))

(defn save-folder [session folder]
  (let [parent (when (:parent folder) (Integer/parseInt (:parent folder)))
        folder (conj folder {:parent parent})
        id (folder-model/save folder)]
    (redirect (str "/folders/" (if (nil? parent) id parent)))))

(defn delete-folder [session params]
  (let [id (:id params)]
    (folder-model/delete-it (:user-id session) id)
    (redirect "/folders")))