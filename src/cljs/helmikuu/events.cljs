(ns helmikuu.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [helmikuu.conf :refer [config]]
   [ajax.core :refer [json-request-format json-response-format]]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(def default-db
  {:name "re-frame"})

(defn slug-url [slug]
  (str (:wordpress-slug-url config) slug))

(def wp-url (str (:wordpress-post-url config)))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

 (re-frame/reg-event-db
  ::set-slug
  (fn-traced [db [_ slug]]
    (assoc db :slug slug)))

(re-frame/reg-event-fx
  ::get-blogpost
  (fn-traced [{:keys [db]} [_ slug]]
    {:http-xhrio {:method          :get
                  :uri             (slug-url slug)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-blogpost-success]
                  :on-failure      [:api-request-error :get-blogpost]}
      :db         (assoc-in db [:loading :blogpost] true)}))

(re-frame/reg-event-db
  :get-blogpost-success
  (fn-traced [db [_ result]]
   (-> db
        (assoc-in [:loading :blogpost] false)
        (assoc :blogpost-api-response result))))

(re-frame/reg-event-fx
  ::get-all-posts
  (fn-traced [{:keys [db]} [_]]
    {:http-xhrio {:method          :get
                  :uri             wp-url
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-all-posts-success]
                  :on-failure      [:api-request-error :all-posts]}
      :db         (assoc-in db [:loading :all-posts] true)}))

(re-frame/reg-event-db
  :get-all-posts-success
  (fn-traced [db [_ result]]
    (-> db
        (assoc-in [:loading :all-posts] false)
        (assoc :all-posts-api-response result))))