(ns app.routes
  (:require
   [app.controllers.version :as version]
   [app.pages.hello :as hello]
   [compojure.api.sweet :refer :all]
   [ring.util.http-response :refer :all]))

(defapi api-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "API"}}}}
  (GET "/" [] (ok (version/version)))
  (context
   "/v1" []

   (GET "/" [] (ok (version/version)))


   (GET "/plus" []
        :return       Long
        :query-params [x :- Long, {y :- Long 1}]
        :summary      "x+y with query-parameters. y defaults to 1."
        (ok (+ x y)))

   (POST "/minus" []
         :return      Long
         :body-params [x :- Long, y :- Long]
         :summary     "x-y with body-parameters."
         (ok (- x y)))

   (GET "/times/:x/:y" []
        :return      Long
        :path-params [x :- Long, y :- Long]
        :summary     "x*y with path-parameters"
        (ok (* x y)))

   (POST "/divide" []
         :return      Double
         :form-params [x :- Long, y :- Long]
         :summary     "x/y with form-parameters"
         (ok (/ x y)))

   (GET "/power" []
        :return      Long
        :header-params [x :- Long, y :- Long]
        :summary     "x^y with header-parameters"
        (ok (long (Math/pow x y))))))


(compojure.core/defroutes page-routes
  (compojure.core/GET "/hello" [] (hello/hello))
  (compojure.core/GET "/docs" []  (hello/docs)))
