(ns helmikuu.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
  ::slug
  (fn [db _]
    (:slug db)))

(re-frame/reg-sub
  ::blogitem
  (fn [db _]
    (:blogitem db)))

(re-frame/reg-sub
  ::blogpost-api-response
  (fn [db _]
    (:blogpost-api-response db)))

(re-frame/reg-sub
  ::all-posts-api-response
  (fn [db _]
    (:all-posts-api-response db)))