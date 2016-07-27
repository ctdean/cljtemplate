(ns app.pages.hello
  "@ctdean"
  (:require
   [app.db.auth :as adb]
   [app.layout :as layout]
   [clojure.java.io :as io]
   [markdown.core :refer [md-to-html-string]]))

(defn hello []
  (layout/render "hello.html"
                 {:now (java.util.Date.)
                  :users (adb/users-read-all)
                  :countries [{:country "Algeria" :number (rand-int 1000)}
                              {:country "Bulgaria" :number (rand-int 1000)}
                              {:country "Cambodia" :number (rand-int 1000)}
                              {:country "Dominica" :number (rand-int 1000)}
                              {:country "Egypt" :number (rand-int 1000)}
                              {:country "France" :number (rand-int 1000)}]}))

(defn docs []
  (layout/render "docs.html"
                 {:body (-> "md/docs.md" io/resource slurp md-to-html-string)}))
