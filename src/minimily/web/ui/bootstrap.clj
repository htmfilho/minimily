(ns minimily.web.ui.bootstrap)

(defn show-field [label object value & [when-true when-false]]
  [:p 
    [:span {:class "label"} label]
    [:br]
    (if (or when-true when-false)
      (if (value object)
        when-true
        when-false)
      (value object))])
