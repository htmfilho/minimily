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

(defn back-button [url]
  [:a {:href url :class "btn btn-outline-secondary"} 
    [:i {:class "fas fa-angle-left"}]
    (str "&nbsp;")
    "Back"])

(defn edit-button [url]
  [:a {:href url :class "btn btn btn-primary"} 
    [:i {:class "fas fa-edit"}]
    (str "&nbsp;")
    "Edit"])

(defn cancel-button [url]
  [:a {:href url :class "btn btn-outline-secondary"} 
    [:i {:class "fas fa-ban"}]
    (str "&nbsp;")
    "Cancel"])