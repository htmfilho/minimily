(ns minimily.web.ui.bootstrap)

(defn show-field [label object value]
  [:p 
    [:span {:class "label"} label]
    [:br]
    (value object)])
