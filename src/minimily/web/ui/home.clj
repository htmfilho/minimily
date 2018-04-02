(ns minimily.web.ui.home
  (:require [minimily.web.ui.layout :refer [layout]]
  [minimily.utils.web.wrapper       :refer [http-headers]]))

(defn home-page [session]
  (http-headers 
    (layout session (:full-name session) 
      (when (not (empty? session))
        [:div {:class "list-group"}
          [:a {:href "/accounts" 
              :class "list-group-item list-group-item-action flex-column 
                      align-items-start"}
            [:div {:class "d-flex w-100 justify-content-between"}
              [:h5 {:class "mb-1"} "Accounts"]]
            [:p {:class "mb-1"} "The accounting to organize your finances."]]
          [:a {:href "/folders" 
              :class "list-group-item list-group-item-action flex-column 
                      align-items-start"}
            [:div {:class "d-flex w-100 justify-content-between"}
              [:h5 {:class "mb-1"} "Documents"]]
            [:p {:class "mb-1"} "The archive to organize your digital documents."]]]))))