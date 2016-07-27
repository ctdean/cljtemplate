(ns app.controllers.version
  (:require
   [clojure.java.shell :as shell]
   [clojure.string :as string]
   [common.date-time :refer [now-iso8601]]
   [common.error :refer [ignore-errors-and-log]]))

(defn- clean-str [s]
  (let [clean (and s (clojure.string/trim s))]
    (if (empty? clean)
        nil
        clean)))

;; Return the version, from the version file made by lein-git-version
(defn- load-file-version [proj]
  (let [pkg (symbol (str proj ".versionator"))]
    (require pkg)
    (deref (ns-resolve pkg (symbol "version")))))

;; Return the version, with fall backs for the dev version.
(defn- load-version [proj]
  (or (clean-str (ignore-errors-and-log (load-file-version proj)))
      (clean-str (System/getenv "APP_VERSION"))
      (clean-str (ignore-errors-and-log (:out (shell/sh "git" "describe"))))
      "dev"))

;; Return the current version of the running app.
(def current-version (memoize (fn [proj] (load-version proj))))

(defn version []
  {:api_version "1"
   :version (current-version "app")
   :time (now-iso8601)})
