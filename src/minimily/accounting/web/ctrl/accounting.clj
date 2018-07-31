(ns minimily.accounting.web.ctrl.accounting
  (:require [minimily.accounting.web.ui.accounting :refer [accounting-page]]))

(defn view-accounting [session]
  (accounting-page session))