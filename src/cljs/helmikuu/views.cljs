(ns helmikuu.views
  (:require
   [re-frame.core :as re-frame]
   [helmikuu.subs :as subs]
   [helmikuu.db :as db]
   [cljs.core.async :as a :refer [<!]]
   [reagent.core :as r])
(:require-macros [cljs.core.async.macros :refer [go]]))

(defonce blogdata (r/atom []))

(defonce request
 (go (let [response (<! (db/get-blog-posts))]
       (reset! blogdata (:body response)))))

(defonce blogitemdata (r/atom []))
(defonce old-slug (atom ""))

(defn header []
(let [panel (re-frame/subscribe [::subs/active-panel])]
  [:header.pt-3.pb-3
   [:div.headeri
    [:nav.nav.nav-fill.flex-column.flex-sm-row.justify-content-center
      [(if (= @panel :home-panel)
        :a.nav-item.nav-link.active
        :a.nav-item.nav-link) {:href "#/"} "Home"]
        [(if (= @panel :blog-panel)
          :a.nav-item.nav-link.active
          :a.nav-item.nav-link) {:href "#/blog"} "Blogi"]
      [(if (= @panel :about-panel)
        :a.nav-item.nav-link.active
        :a.nav-item.nav-link) {:href "#/about"} "About"]]]]))

;; home
(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.container
    [header]
      [:div.container.pt-4
      [:h1 "Wellcome"]
           [:div
            [:a {:href "#/about"}
             "go to About Page"]]
             [:div
              [:a {:href "#/blog"}
               "go to Blog Page"]]]]))


;; about

(defn about-panel []
[:div.container
  [header]
  [:div.container.pt-4
     [:h1 "This is the About Page."]

     [:div
      [:a {:href "#/"}
       "go to Home Page"]]]])

 ;; blog

 (defn blog-panel []
   (let [data @blogdata]
  [:div.container
  [header]
  [:div.container.pt-4
      (map (fn [blogitem]
             [:div {:key (:id blogitem)}
              [:h2 [:a {:href (str "#/blog/" (:slug blogitem))} (:rendered (:title blogitem))]]
              [:p {:dangerouslySetInnerHTML {:__html (:rendered (:excerpt blogitem))}}]
              [:a {:href "#"}]]) data)]]))

(defn blogitem-panel []
(let [slug (re-frame/subscribe [::subs/slug])]
    (if (not= @old-slug slug)
    (do
      (go (let [response (<! (db/get-one-post @slug))]
          (reset! blogitemdata (:body response))))
          (reset! old-slug slug)))
    [:div.container
    [header]
    [:div.container.pt-4
      [:h2 (:rendered (:title (first @blogitemdata)))]
      [:p {:dangerouslySetInnerHTML {:__html (:rendered (:content (first @blogitemdata)))}}]]]))


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :blog-panel [blog-panel]
    :blogitem-panel [blogitem-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
