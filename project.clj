(defproject minimily "0.1.0-SNAPSHOT"
  :description "Minimily is an application designed to help you keeping your
                life as simple as possible. We will help you to make decisions
                taking into account the impact on your health, family, living
                place, community and environment."
  :url "http://minimily.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure       "1.8.0" ]  ; recent version of Clojure
                 [environ                   "1.1.0" ]  ; manage environment variables

                 [org.clojure/java.jdbc     "0.7.0" ]  ; jdbc api
                 [org.postgresql/postgresql "42.1.4"]  ; db driver
                 [honeysql                  "0.9.0" ]  ; sql abstraction
                 [hikari-cp                 "1.7.6" ]  ; connection pool
                 [ragtime                   "0.7.1" ]  ; migration
                 [org.slf4j/slf4j-nop       "1.7.13"]  ; hikari-cp's dependency

                 [compojure                 "1.6.0" ]  ; routing library
                 [ring/ring-jetty-adapter   "1.4.0" ]] ; web application library
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "minimily-standalone.jar"
  :profiles {:production {:env {:production true}}})
