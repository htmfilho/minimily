# Minimily

Minimily is an application designed to help you keeping your life as simple as
possible. We will help you to make decisions taking into account the impact on
your health, family, living place, community and environment.

## Usage

Run from the terminal (dev profile):

    $ lein run

Run from the REPL (dev profile):

    $ lein repl
    minimily.web=> (start-server 3000)
    minimily.web=> (stop-server)

Package the application into a `jar` file (uberjar profile with prod config):

    $ lein uberjar

Run the `jar` package:

    $ java $JVM_OPTS -cp target/minimily-standalone.jar clojure.main -m minimily.web

To deploy to Heroku, make sure you have a PostgreSQL database and an 
environment variable named DATABASE_URL.

## License

Copyright © 2017 Hildeberto Mendonca

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
