(ns app.main
  (:require
   [app.auth :as auth]
   [app.db.core]                        ; Make sure the db connection loads
   [app.routes]
   [app.controllers.version :as version]
   [backtick.core :as bt]
   [cheshire.core :as json]
   [clams.conf :as conf]
   [clams.migrate :as migrate]
   [clojure.tools.logging :as log]
   [common.convert :refer [str->int]]
   [compojure.core :refer [routes wrap-routes]]
   [compojure.route :as route]
   [org.httpkit.server :as http-kit]
   [prone.middleware :refer [wrap-exceptions]]
   [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
   [ring.middleware.reload :refer [wrap-reload]]
   [selmer.middleware :refer [wrap-error-page]]
   )
  (:gen-class))

(def all-routes
  (routes
   #'app.routes/api-routes
   #'app.routes/page-routes
   (route/not-found (auth/json-response {:error "missing"} 404))))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t)
        (auth/json-response {:error (str "Internal Error")} 500)))))

(defn wrap-log-request [handler]
  (fn [req]
    (log/debugf "%s - %s \"%s %s\" %s"
                (:remote-addr req)
                "-"
                (:request-method req)
                (:uri req)
                (:identity req))
    (handler req)))

(defn wrap-base [handler]
  (let [base (fn [h] (wrap-defaults h
                                    (-> api-defaults
                                        (assoc-in [:security :anti-forgery] false))))
        pre (-> handler
                wrap-log-request
                ;; auth/wrap-auth
                )]
    (if (not (conf/get :debug?))
        (-> pre
            wrap-internal-error
            base)
        (-> pre
            wrap-reload
            wrap-error-page
            wrap-exceptions
            base))))

(defn app [] (wrap-base #'all-routes))

(defn start-worker []
  (log/infof "Starting worker version %s" (:version (version/version)))
  (bt/start))

(defonce server (atom nil))

(defn start-web []
  (let [port (str->int (conf/get :port))]
    (log/infof "Starting web version %s on port %s"
               (:version (version/version))
               port)
    (reset! server (http-kit/run-server (app) {:port port}))))

(defn start [args]
  (let [cmd (or (first args) "both")]
    (case cmd
      "web" (start-web)
      "worker" (start-worker)
      "both" (do (start-web) (start-worker))
      "migrate" (migrate/-main "migrate")
      )))

(defn -main [& args]
  (start args))
