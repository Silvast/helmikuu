(ns helmikuu.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [secretary.core :as secretary]
   [goog.events :as gevents]
   [goog.history.EventType :as EventType]
   [re-frame.core :as re-frame]
   [helmikuu.events :as events]
   [helmikuu.handlers :as handlers]))

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (re-frame/dispatch [::events/set-active-panel :home-panel])
    )

  (defroute "/about" []
    (re-frame/dispatch [::events/set-active-panel :about-panel]))

(defroute "/blog" []
  (re-frame/dispatch [::events/set-active-panel :blog-panel]))

(defroute "/blog/:slug" [slug]
  (re-frame/dispatch [::events/set-slug slug])
  (re-frame/dispatch [::events/set-active-panel :blogitem-panel]))

  ;; --------------------
  (hook-browser-navigation!))
