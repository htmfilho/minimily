# Minimily

Minimily is an application designed to help you keeping your life as simple as possible. We will help you to make decisions taking into account the impact on your health, family, living place, community and environment.

## Installation

Minimily is a web application and it has 3 requirements:

 1. *PostgreSQL* is a database that scales from a Raspberry PI to a high availability cluster. It requires some additional configuration at the beginning, but it is quite straightforward to work with. 

 2. The application is developed in Clojure, a language hosted on the *Java Virtual Machine* (JVM), which means it depends on a Java environment to work. The JVM is well known for its performance and large ecosystem.

 3. *Leiningen* is a build tool for Clojure applications. It is used during development, packaging and deployment.

### Installing PostgreSQL

Execute the following commands to install PostgreSQL:

    $ sudo apt-get install postgresql
    $ sudo su - postgres -c "createuser -s $USER"

Then create the database for the application:

    $ createdb minimily
    $ createuser minimily -P
    $ psql -d minimily
      =# grant connect on database minimily to minimily;
      =# \q

### Installing JVM

### Installing Leiningen

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
