(ns minimily.accounting.web.ui.accounting
  (:require [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [minimily.web.model.menu    :refer [menu-items]]
            [minimily.web.ui.home       :refer [menu-item-template]]))

(defn accounting-page [session]
  (http-headers 
    (layout session (:full-name session) 
      [:div {:class "list-group"}
        (map menu-item-template 
             (:submenu (first (filter #(= (:link %) "/accounting") 
                                      menu-items))))])))