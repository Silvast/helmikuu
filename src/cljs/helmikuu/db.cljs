(ns helmikuu.db
  (:require [cljs-http.client :as http]
            [helmikuu.conf :refer [config]])
  (:refer-clojure :exclude [slurp]))

(def default-db
  {:name "re-frame"})

(def wp-url (str (:wordpress-post-url config)))

(defn slug-url [slug]
  (str (:wordpress-slug-url config) slug))

(defn get-blog-posts []
  (http/get wp-url {:with-credentials? false}))

(defn get-one-post [slug]
  (http/get (slug-url slug) {:with-credentials? false}))

