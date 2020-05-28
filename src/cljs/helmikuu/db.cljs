(ns helmikuu.db
  (:require [cljs-http.client :as http]
            [helmikuu.conf :refer [config]])
  (:refer-clojure :exclude [slurp]))

(def default-db
  {:name "re-frame"})

(def wp-url (str (:wordpress-post-url config)))

(defn slug-url [slug]
  (str (:wordpress-post-url config) "/slug:" slug))

(defn get-blog-posts []
  (apply js/console.log config2)
  (http/get wp-url {:with-credentials? false}))

(defn get-one-post [slug]
  (http/get (slug-url slug) {:with-credentials? false}))

