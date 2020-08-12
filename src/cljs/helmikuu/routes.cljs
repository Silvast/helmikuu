(ns helmikuu.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [secretary.core :as secretary]
   [goog.events :as gevents]
   [goog.history.EventType :as EventType]
   [re-frame.core :as re-frame]
   [helmikuu.events :as events]))

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; -------------------
  ;; define routes here
  (defroute "/" []
    (do
      (re-frame/dispatch [::events/get-all-posts])
      (re-frame/dispatch [::events/set-active-panel :home-panel])))

  (defroute "/about" []
    (do
      (re-frame/dispatch [::events/set-slug "about"])
      (re-frame/dispatch [::events/get-blogpost "about"])
      (re-frame/dispatch [::events/set-active-panel :about-panel])))

  (defroute "/english" []
    (do
      (re-frame/dispatch [::events/set-slug "english"])
      (re-frame/dispatch [::events/get-blogpost "english"])
      (re-frame/dispatch [::events/set-active-panel :english-panel])))

  (defroute "/blog" []
    (do
      (re-frame/dispatch [::events/get-all-posts])
      (re-frame/dispatch [::events/set-active-panel :blog-panel])))

  (defroute "/blog/:slug" [slug]
    (do
      (re-frame/dispatch [::events/set-slug slug])
      (re-frame/dispatch [::events/get-blogpost slug])
      (re-frame/dispatch [::events/set-active-panel :blogitem-panel])
      (re-frame/dispatch [::events/set-title])))

  ;; --------------------
  (hook-browser-navigation!))
