(ns minimily.web.ui.home
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [minimily.web.model.menu    :refer [menu-items]]))

(defn menu-item-template [menu-item]
  [:a {:href (:link menu-item)
       :class "list-group-item list-group-item-action flex-column 
               align-items-start"}
    [:div {:class "d-flex w-100 justify-content-between"}
      [:h3 {:class "mb-1"} (:label menu-item)]]
    [:p {:class "mb-1"} (:description menu-item)]])


(defn home-page [session]
  (http-headers 
    (layout session (:full-name session) 
      (when (not (empty? session))
        [:div {:class "list-group"}
          (map menu-item-template menu-items)]))))