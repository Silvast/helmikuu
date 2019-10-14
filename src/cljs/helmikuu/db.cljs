(ns helmikuu.db
  (:require [cljs-http.client :as http]
            [helmikuu.conf :refer [config]])
  (:refer-clojure :exclude [slurp]))

(def default-db
  {:name "re-frame"})

(def wp-url (str (:wordpress-host config) "/wp-json/wp/v2/posts"))
(defn slug-url [slug]
  (str (:wordpress-host config) "/wp-json/wp/v2/posts?slug=" slug))

(defn get-blog-posts []
  (apply js/console.log config2)
  (http/get wp-url))

(defn get-one-post [slug]
  (http/get (slug-url slug)))

