(ns minimily.web.ctrl.home
  (:require [ring.util.response         :refer [response]]
            [minimily.web.ui.layout     :refer [layout]]
            [minimily.utils.web.wrapper :refer [http-headers]]
            [minimily.web.model.menu    :refer [menu-items]]
            [minimily.web.ui.home       :as home-ui]))

(defn home-page [session]
  (let [content (home-ui/home-page session)]
    (if (empty? session)
      (-> content
          (assoc :session {}))
      content)))