(ns minimily.inventory.web.ui.inventory
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [minimily.web.model.menu    :refer [menu-items]]
            [minimily.web.ui.home       :refer [menu-item-template]]))

(defn inventory-page [session]
  (http-headers 
    (layout session (:full-name session) 
      [:div {:class "list-group"}
        (map menu-item-template 
             (:submenu (first (filter #(= (:link %) "/inventory") 
                                      menu-items))))])))