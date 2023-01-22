(ns minimily.documents.web.ctrl.folder
  (:require [ring.util.response                    :refer [redirect]]
            [minimily.documents.web.ui.folder      :as folder-view]
            [minimily.documents.model.folder       :as folder-model]
            [minimily.documents.model.document     :as document-model]))

(defn merge-num-children [profile-id folders]
  (map #(conj % {:num-children (document-model/count-siblings profile-id (:id %))})
       folders))

(defn merge-documents [folder-id folders]
  (let [documents (document-model/find-by-folder folder-id)]
    (reduce conj documents folders)))

(defn view-parent-folders [session]
  (let [folders (merge-num-children (:profile-id session) 
                                    (folder-model/find-parents (:profile-id session)))]
    (folder-view/folders-page session folders)))

(defn view-folder [session id]
  (let [profile-id (:profile-id session)
        folder-id  (Integer/parseInt id)
        folder     (folder-model/get-it profile-id folder-id)
        children   (merge-documents folder-id
                                   (merge-num-children profile-id
                                                       (folder-model/find-children profile-id folder-id)))
        path (folder-model/find-path profile-id folder-id)]
    (folder-view/folder-page session folder children path)))

(defn new-folder [session parent-id]
  (let [parent (folder-model/get-it (:profile-id session) parent-id)]
    (folder-view/folder-form-new session parent)))

(defn edit-folder [session id]
  (let [folder (folder-model/get-it (:profile-id session) id)]
    (folder-view/folder-form-edit session folder)))

(defn save-folder [session folder]
  (let [parent (when (:parent folder) (Integer/parseInt (:parent folder)))
        folder (conj folder {:parent parent :profile (:profile-id session)})
        id (folder-model/save folder)]
    (redirect (str "/folders/" (if (nil? parent) id parent)))))

(defn delete-folder [session params]
  (let [folder-id (Integer/parseInt (:id params))]
    (folder-model/delete-it (:profile-id session) folder-id)
    (redirect "/folders")))