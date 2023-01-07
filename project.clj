(defproject minimily "0.1.0-SNAPSHOT"
  :description "Minimily is an application designed to help you keeping your
                life as simple as possible. We will help you to make decisions
                taking into account the impact on your health, family, living
                place, community and environment."
  :url "http://minimily.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure       "1.10.2" ]  ; recent version of Clojure

                 ; Database dependencies
                 [org.clojure/java.jdbc     "0.7.0"  ]  ; jdbc api
                 [org.postgresql/postgresql "42.3.1" ]  ; db driver
                 [com.layerware/hugsql      "0.5.3"  ]  ; sql abstraction
                 [hikari-cp                 "2.10.0" ]  ; connection pool (https://github.com/tomekw/hikari-cp)
                 [ragtime                   "0.8.0"  ]  ; migration
                 [org.slf4j/slf4j-nop       "1.7.13" ]  ; hikari-cp's dependency

                 ; Web dependencies
                 [buddy                     "2.0.0"  ]  ; web security
                 [compojure                 "1.6.1"  ]  ; routing library
                 [hiccup                    "1.0.5"  ]  ; html template library
                 [selmer                    "1.12.12"]  ; template system used for email messages
                 [ring/ring-core            "1.6.3"  ]  ; web application library
                 [http-kit                  "2.3.0"  ]
                 [ring/ring-devel           "1.6.3"  ]  ; web development facilities
                 [com.draines/postal        "2.0.3"  ]  ; email library
                 [javax.servlet/servlet-api "2.5"    ]
                 
                 ; Extra dependencies
                 [yogthos/config            "1.1.4"  ]  ; manage environment variables
                 [amazonica                 "0.3.121"]  ; access to amazon web services
                 [org.clojure/data.json     "0.2.6"  ]  ; data structures to json
                 [clj-time                  "0.15.2" ]]

  :plugins [[io.taylorwood/lein-native-image "0.3.0"]
            [nrepl/lein-nrepl "0.3.2"]]

  :min-lein-version "2.0.0"
  
  :aot :all
  
  :main minimily.app

  :native-image {:name "minimily"
                 :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                 :opts     ["--enable-url-protocols=http"
                            "--report-unsupported-elements-at-runtime"
                            "--initialize-at-build-time"
                            "--allow-incomplete-classpath"
                            "--no-server"
                            "--initialize-at-run-time=org.postgresql.sspi.SSPIClient"
                            "-H:ConfigurationResourceRoots=resources"
                            ~(str "-H:ResourceConfigurationFiles="
                                  (System/getProperty "user.dir")
                                  (java.io.File/separator)
                                  "resource-config.json")]}

  :uberjar-name "minimily-standalone.jar"
  :profiles {:prod {:resource-paths ["config/prod"]}
             :dev  {:resource-paths ["config/dev"]} ; it runs when using the repl.
             :uberjar {:aot :all :resource-paths ["config/prod"]}}) ;
