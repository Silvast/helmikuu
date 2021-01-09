(ns helmikuu.views
  (:require
   [re-frame.core :as re-frame]
   [helmikuu.subs :as subs]
   [cljs.core.async :as a :refer [<!]]
   [cljs-http.client :as http]
   [cljs-time.core :as time]
   [cljs-time.format :as f]
   [helmikuu.conf :refer [config]]
   [reagent.core :as r]
   [goog.object])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn header []
  (let [panel (re-frame/subscribe [::subs/active-panel])
        document-title (re-frame/subscribe [::subs/title])]
    (goog.object/set js/document "title" @document-title)
    (js/window.scrollTo 0 0)
    [:header
     [:nav.navbar.navbar-expand-lg.navbar-light
      [:a.navbar-brand {:href "#/"}]
      [:button.navbar-toggler
       {:type "button" :data-toggle "collapse" :data-target "#navbarSupportedContent"}
       [:span.navbar-toggler-icon]]
      [:div.collapse.navbar-collapse {:id "navbarSupportedContent"}
       [:ul.navbar-nav.ml-auto
        [:li.nav-item
         [(if (= @panel :home-panel)
            :a.nav-link.active
            :a.nav-link) {:href "#/"} "Etusivu"]]
        [:li.nav-item
         [(if (or (= @panel :blog-panel) (= @panel :blogitem-panel))
            :a.nav-link.active
            :a.nav-link) {:href "#/blog"} "Blogi"]]
        [:li.nav-item
         [(if (= @panel :about-panel)
            :a.nav-link.active
            :a..nav-link) {:href "#/about"} "Minä"]]
        [:li.nav-item
         [(if (= @panel :english-panel)
            :a.nav-link.active
            :a..nav-link) {:href "#/english"} "In English"]]]]]]))

;; Footer


(defn footer []
  [:footer.main-footer
   [:div.container
    [:div.row
     [:div.col-md-4 [:p [:a {:href "https://github.com/Silvast"} [:i.fa.fa-github {:style {"fontSize" "36px"}}]]]]
     [:div.col-md-4 [:p [:a {:href "https://twitter.com/AnskuSilvast"} [:i.fa.fa-twitter {:style {"fontSize" "36px"}}]]]]
     [:div.col-md-4 [:p [:a {:href "https://www.linkedin.com/in/silvast/"} [:i.fa.fa-linkedin {:style {"fontSize" "36px"}}]]]]]]])

;; Twitterpanel
;; home


(defn home-panel []
  (let [blogs @(re-frame/subscribe [::subs/all-posts-api-response])]
    [:div.main
     [header]
     [:div.siteintro
      [:div.text [:h2 "Olen Ansku."]]]
     [:section.latest-posts
      [:div.container
       [:div.row.me-card
        [:div.col-md-6.col-12
         [:div.frontpage-image
          [:img.img-fluid {:src "https://anskufail.files.wordpress.com/2020/06/anskuit-e1593619460448.jpg"}]
          [:div.pixel-overlay]]]
        [:div.col-md-6.col-12
         [:p "Moikka, oon Ansku! Devaan ja yleistekkeilen,
       märsään ihmisoikeuksista, työelämästä, sijoittamisesta ja kaikesta randomista. Työkseni teen
       nykyään hommia Ylellä koodaavana Team Leadina" [:a {:href "https://yle.dev/"} " yle.dev:llä."].  ]
         [:p [:a {:href "#/about"} " Lue lisää."]]]]]]
     [:div.blogheading
      [:h2 "Viimeisimmät blogikirjoitukset"]]
     [:section.latest-posts
      [:div.container-fluid
       [:div.row
        (map (fn [blogitem]
               [:div.col-xl-4.col-lg-4.col-md-12 {:key (:ID blogitem)}
                [:div.blog-card
                 [:div.post-thumbnail [:a {:href (str "#/blog/" (:slug blogitem))} [:img.img-fluid {:src (:URL (:post_thumbnail blogitem))}]]]
                 [:div.post-meta.row
                  [:div.date.col-lg-6.col-md-4.col-4 (take 10 (:date blogitem))]
                  [:div.category.col-lg-6.col-md-4.col-4 (:name (get-in (:categories blogitem) (keys (:categories blogitem))))]]
                 [:a {:href (str "#/blog/" (:slug blogitem))} [:h3.h4 (:title blogitem)]]
                 [:p.text-muted {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
                 [:a {:href (str "#/blog/" (:slug blogitem))} "lue lisää.."]]]) (take 3 (:posts blogs)))]]]
     [:div.blogheading [:a.btn.btn-primary {:href "#/blog"} "Kaikki postaukset"]]
     [footer]]))

;; about

(defn about-panel []
  (let [about-api-response (re-frame/subscribe [::subs/blogpost-api-response])]
    [:div.main.about
     [header]
     [:div.container.pt-4
      [:div.row.aboutrow
       [:div.col-sm
        [:div.container
         [:article
          [:h1 "Minä olen Anne-Mari Silvast."]
          [:p.text-bigger {:dangerouslySetInnerHTML {:__html (:content @about-api-response)}}]]]]]]
     [footer]]))

 ;; blog

(defn blog-panel []
  (let [data @(re-frame/subscribe [::subs/all-posts-api-response])]
    [:div.main
     [header]
     [:div.container.about
      (map (fn [blogitem]
             [:div {:key (:ID blogitem)}
              [:h1 [:a {:href (str "#/blog/" (:slug blogitem))} (:title blogitem)]]
              [:div.post-meta.row
               [:div.date.col-md-2.col-3 (take 10 (:date blogitem))]
               [:div.category.md-2.col-3 (:name (get-in (:categories blogitem) (keys (:categories blogitem))))]]
              [:p {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
              [:a {:href "#"}]]) (:posts data))]
     [footer]]))

(defn blogitem-panel []
  (let [blogpost-api-response (re-frame/subscribe [::subs/blogpost-api-response])]
    [:div.main.about
     [header]
     [:div.container.pt-4.pl-4.pr-4
      [:div.row
       [:div.col-sm
        [:article
         [:h1 (:title @blogpost-api-response)]
         [:div.post-meta.row
          [:div.date.col-md-2.col-3 (take 10 (:date @blogpost-api-response))]
          [:div.category.md-2.col-3 (:name (get-in (:categories @blogpost-api-response) (keys (:categories @blogpost-api-response))))]]
         [:p {:dangerouslySetInnerHTML {:__html (:content @blogpost-api-response)}}]]]]]
     [footer]]))
;; main

(defn english-panel []
  (let [english-api-response (re-frame/subscribe [::subs/blogpost-api-response])]
    [:div.main.about
     [header]
     [:div.container.pt-4
      [:div.row.aboutrow
       [:div.col-sm
        [:div.container
         [:article
          [:h1 "About Anne-Mari Silvast"]
          [:p.text-bigger {:dangerouslySetInnerHTML {:__html (:content @english-api-response)}}]]]]]]
     [footer]]))

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :blog-panel [blog-panel]
    :blogitem-panel [blogitem-panel]
    :english-panel [english-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
