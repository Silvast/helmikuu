(ns helmikuu.views
  (:require
   [re-frame.core :as re-frame]
   [helmikuu.subs :as subs]
   [cljs.core.async :as a :refer [<!]]
   [cljs-http.client :as http]
   [helmikuu.conf :refer [config]]
   [reagent.core :as r])
  (:require-macros [cljs.core.async.macros :refer [go]]))

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
      [:div.row
       [:div.col-sm [:h1 "Anne-Mari Silvast"]]
       [:div.col-sm [:h2 "Testi"]]]
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
  (let [data @(re-frame/subscribe [::subs/all-posts-api-response])]
    [:div.container
     [header]
     [:div.container.pt-4
      (map (fn [blogitem]
             [:div {:key (:ID blogitem)}
             [:h2 [:a {:href (str "#/blog/" (:slug blogitem))} (:title blogitem)]]
              [:p {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
              [:a {:href "#"}]]) (:posts data))]]))

(defn blogitem-panel []
  (let [blogpost-api-response (re-frame/subscribe [::subs/blogpost-api-response])]
    [:div.container
     [header]
     [:div.container.pt-4
      [:h2 (:title @blogpost-api-response)]
      [:p {:dangerouslySetInnerHTML {:__html (:content @blogpost-api-response)}}]]]))
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
