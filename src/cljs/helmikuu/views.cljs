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
    [:header.header
     [:nav.navbar.navbar-expand-lg
      [:div.container
       [:div.navbar-header.d-flex.align-items-center.justify-content-between
        [:a.navbar-brand {:href "#/"}
        ; [:img.img-fluid {:src "https://anskufail.files.wordpress.com/2020/07/anskuicon2.png"}]
         ]]
       [:div#navbarcollapse.collapse.navbar-collapse.show
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
             :a..nav-link) {:href "#/about"} "Minä"]]]]]]]))

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
      [:div.text [:h2 "Olen Ansku."]]
        ; [:div.frontpage-image
        ;   [:img.img-fluid {:src "https://anskufail.files.wordpress.com/2020/07/anskunen-min.png"}]
        ;   [:div.text [:h2 "Olen Ansku."]]
        ;   [:div.pixel-overlay]]
          ]
          [:section.latest-posts
            [:div.container.text-bigger
            [:div.row.me-card
              [:div.col-md-4
              [:div.frontpage-image
                [:img.img-fluid {:src "https://anskufail.files.wordpress.com/2020/06/anskuit-e1593619460448.jpg"}]
               [:div.pixel-overlay]]]
        [:div.col-md-8
         [:p "Devaan ja yleistekkeilen,
       märsään ihmisoikeuksista, työelämästä ja sijoittamisesta. Työkseni teen
       teknisen projarin hommia.  "]
          [:p [:a {:href "#/about"} " Lue lisää."]]]]]]
     [:div.blogheading
      [:h2 "Viimeisimmät blogikirjoitukset"]]
     [:section.latest-posts
      [:div.container
       [:div.row
        (map (fn [blogitem]
               [:div.col-md-4 {:key (:ID blogitem)}
                [:div.blog-card
                 [:div.post-thumbnail [:a {:href (str "#/blog/" (:slug blogitem))} [:img.img-fluid {:src (:URL (:post_thumbnail blogitem))}]]]
                 [:div.post-meta.d-flex.justify-content-between
                  [:div.date (take 10 (:date blogitem))]
                  [:div.category (:name (:tekki (:categories blogitem)))]]
                 [:a {:href (str "#/blog/" (:slug blogitem))} [:h3.h4 (:title blogitem)]]
                 [:p.text-muted {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
                 [:a {:href (str "#/blog/" (:slug blogitem))} "lue lisää.."]]]) (take 3 (:posts blogs)))]]]
     [:div.blogheading
      [:h2 "Twiittailut"]]
     [:section.latest-posts
      [:div.container
       [:div.row
        ; [:div [:div {:dangerouslySetInnerHTML {:__html "<a class=\"twitter-timeline\"
        ; href=\"https://twitter.com/AnskuSilvast?ref_src=twsrc%5Etfw\">Tweets by
        ; AnskuSilvast</a> <script async src=\"https://platform.twitter.com/widgets.js\"
        ; charset=\"utf-8\"></script>"}}] ] 
        ]]]
     [footer]]))

;; about


(defn about-panel []
  [:div.main
   [header]
   [:div.container.pt-4.about
    [:h1 "Minä olen Anne-Mari Silvast."]

    [:div
     [:h2 "Bio"]]]
   [footer]])

 ;; blog

(defn blog-panel []
  (let [data @(re-frame/subscribe [::subs/all-posts-api-response])]
    [:div.main
     [header]
     [:div.container.pt-4.about
      (map (fn [blogitem]
             [:div {:key (:ID blogitem)}
              [:h1 [:a {:href (str "#/blog/" (:slug blogitem))} (:title blogitem)]]
              [:p {:dangerouslySetInnerHTML {:__html (:excerpt blogitem)}}]
              [:a {:href "#"}]]) (:posts data))]
     [footer]]))

(defn blogitem-panel []
  (let [blogpost-api-response (re-frame/subscribe [::subs/blogpost-api-response])]
    [:div.main.about
     [header]
     [:div.container.pt-4
      [:div.row
       [:div.col-sm
        [:article
         [:h1 (:title @blogpost-api-response)]
         [:p.text-bigger {:dangerouslySetInnerHTML {:__html (:content @blogpost-api-response)}}]]]]]
     [footer]]))
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
