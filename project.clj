(defproject minimily "0.1.0-SNAPSHOT"
  :description "Minimily is an application designed to help you keeping your
                life as simple as possible. We will help you to make decisions
                taking into account the impact on your health, family, living
                place, community and environment."
  :url "http://minimily.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure       "1.9.0"  ]  ; recent version of Clojure
                 
                 ; Database dependencies
                 [org.clojure/java.jdbc     "0.7.0"  ]  ; jdbc api
                 [org.postgresql/postgresql "42.1.4" ]  ; db driver
                 [honeysql                  "0.9.0"  ]  ; sql abstraction
                 [hikari-cp                 "1.8.1"  ]  ; connection pool
                 [ragtime                   "0.7.1"  ]  ; migration
                 [org.slf4j/slf4j-nop       "1.7.13" ]  ; hikari-cp's dependency

                 ; Web dependencies
                 [compojure                 "1.6.0"  ]  ; routing library
                 [hiccup                    "1.0.5"  ]  ; html template library
                 [ring/ring-core            "1.6.3"  ]  ; web application library
                 [ring/ring-jetty-adapter   "1.6.3"  ]  ; web server library 
                 [ring/ring-devel           "1.6.3"  ]
                 
                 ; Extra dependencies
                 [yogthos/config            "1.1.1"  ]  ; manage environment variables
                 [amazonica                 "0.3.121"]]
  :min-lein-version "2.0.0"
  :main ^:ship-aot minimily.app
  :uberjar-name "minimily-standalone.jar"
  :profiles {:prod {:resource-paths ["config/prod"]}
             :dev  {:resource-paths ["config/dev"]} ; it runs when using the repl.
             :uberjar {:aot :all :resource-paths ["config/prod"]}}) ;
