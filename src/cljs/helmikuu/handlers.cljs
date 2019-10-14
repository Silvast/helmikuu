(ns helmikuu.handlers
  (:require
   [re-frame.core :as re-frame]))

(re-frame/register-handler
 :set-slug
 (fn [db [_ slug]]
   (assoc db :slug slug)))
