(ns minimily.inventory.web.ctrl.inventory
  (:require [minimily.inventory.web.ui.inventory :refer [inventory-page]]))

(defn view-inventory [session]
  (inventory-page session))