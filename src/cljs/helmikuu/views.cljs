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
    [:header.header
     [:nav.navbar.navbar-expand-lg
      [:div.container
       [:div.navbar-header.d-flex.align-items-center.justify-content-between
        [:a.navbar-brand {:href "#/"} "Anne-Mari Silvast"]]
       [:div#navbarcollapse.collapse.navbar-collapse
        [:ul.navbar-nav.ml-auto
         [:li.nav-item
          [(if (= @panel :home-panel)
             :a.nav-link.active
             :a.nav-link) {:href "#/"} "Home"]]
         [:li.nav-item
          [(if (or (= @panel :blog-panel) (= @panel :blogitem-panel))
             :a.nav-link.active
             :a.nav-link) {:href "#/blog"} "Blogi"]]
         [:li.nav-item
          [(if (= @panel :about-panel)
             :a.nav-link.active
             :a..nav-link) {:href "#/about"} "About"]]]]]]]))

;; home


(defn home-panel []
  (let [blogs @(re-frame/subscribe [::subs/all-posts-api-response])]
     [:div
    [header]
     [:section.latest-posts
      [:div.container
       [:header
        [:h2 "Viimeisimmät blogikirjoitukset"]
        [:p.text-big "Märsäystä tekniikasta, kirjoista ja muusta randomista."]]
          [:div.row
          (map (fn [blogitem]
            [:div.pos.col-md-4 {:key (:ID blogitem)}
            [:div.post-thumbnail [:a {:href (str "#/blog/" (:slug blogitem))} [:img.img-fluid {:src (:url (:post_thumbnail blogitem))}]]]
             ;;[:h1 [:a {:href (str "#/blog/" (:slug blogitem))} (:title blogitem)]]
             [:p {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
             [:a {:href "#"}]]) (:posts blogs))
          ]]]]))
        ; (map (fn [blogitem]
        ;        [:div.pos.col-md-4 {:key (:ID blogitem)}
        ;         [:div.post-thumbnail [:a {:href (str "#/blog/" (:slug blogitem))} [:img.img-fluid {:src (:url (:post_thumbnail blogitem))}]]]
        ;         ; [:div.post-details
        ;         ;  [:div.post-meta.d-flex.justify-content-between [:div.date (:date blogitem)] [:div.category (:name (first (:categories blogitem)))]]
        ;         ;  [:a {:href (str "#/blog/" (:slug blogitem))} [:h3.h4 (:title blogitem)]]
        ;         ;  [:p.text-muted {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}]]
        ;          ]) (:posts blogs))
                 
        ;          ]]
        ;          ]
        ;          ]))


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
              [:h1 [:a {:href (str "#/blog/" (:slug blogitem))} (:title blogitem)]]
              [:p {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
              [:a {:href "#"}]]) (:posts data))]]))

(defn blogitem-panel []
  (let [blogpost-api-response (re-frame/subscribe [::subs/blogpost-api-response])]
    [:div.container
     [header]
     [:div.container.pt-4
      [:div.row
       [:div.col-sm
        [:article
         [:h1 (:title @blogpost-api-response)]
         [:p {:dangerouslySetInnerHTML {:__html (:content @blogpost-api-response)}}]]]]]]))
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
