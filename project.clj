(defproject app
  "0.1.0"
  :main app.main
  :dependencies [
                 [bouncer "1.0.0"]
                 [clams "0.3.1" :exclusions [ring prismatic/schema]]
                 [buddy "1.0.0"]
                 [clj-time "0.11.0"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.2"]
                 [com.carouselapps/to-jdbc-uri "0.5.0"]
                 [com.layerware/hugsql "0.4.7"]
                 [compojure "1.5.0"]
                 [cprop "0.1.7"]
                 [crypto-random "1.2.0"]
                 [ctdean/backtick "0.7.0"]
                 [http-kit "2.1.19"]
                 [http-kit.fake "0.2.2"]
                 [instaparse "1.4.1"]
                 [luminus-log4j "0.1.3"]
                 [markdown-clj "0.9.89"]
                 [medley "0.5.5"]
                 [metosin/compojure-api "1.1.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [mount "0.1.10"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/test.check "0.9.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.ocpsoft.prettytime/prettytime "3.2.7.Final"]
                 [org.postgresql/postgresql "9.4-1206-jdbc4"]
                 [org.slf4j/slf4j-log4j12 "1.7.13"]
                 [org.webjars.bower/tether "1.3.2"]
                 [org.webjars/bootstrap "4.0.0-alpha.2"]
                 [org.webjars/font-awesome "4.6.2"]
                 [org.webjars/jquery "2.2.3"]
                 [prone "1.1.1"]
                 [ring "1.3.1"]
                 [ring-cors "0.1.4"]
                 [ring-middleware-format "0.7.0"]
                 [ring-server "0.4.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-devel "1.4.0"]
                 [ring/ring-mock "0.3.0"]
                 [robert/hooke "1.3.0"]
                 [selmer "1.0.4"]
                 [st/common "0.10.4" :exclusions [com.cemerick/clojurescript.test]]
                 ]
  :jvm-opts ["-server"]
  :uberjar-name "app.jar"
  :uberjar {:aot :all}
  :plugins [
            [jonase/eastwood "0.1.4"]
            [ctdean/versionator "0.9.1"]
            [lein-test-out "0.3.1"]
            [lein-heroku "0.5.3"]
            ]
  :profiles {
             :uberjar {:aot :all
                       :uberjar-name "app.jar"}
             :test {:jvm-opts ["-Dclams.env=test"]}
             :prod {:jvm-opts ["-Dclams.env=prod"]}
             })
