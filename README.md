# Minimily

Minimily is an application designed to help you keeping your life as simple as possible. We will help you to make decisions taking into account the impact on your health, family, living place, community and environment.

[![CircleCI](https://circleci.com/gh/htmfilho/minimily.svg?style=svg)](https://circleci.com/gh/htmfilho/minimily)

## Installing

### Requirements

Minimily is a web application and it has 3 requirements: 

 1. The application is developed in Clojure, a language hosted on the *Java Virtual Machine* (JVM), which means it depends on a Java environment to work. The JVM is well known for its performance and large ecosystem.

 2. *Leiningen* is a build tool for Clojure applications. It is used during development, packaging and deployment.

 3. Minimily uses an embedded database that works out of the box, but in case you need more capacity, you can use *PostgreSQL* instead. PostgreSQL is a database that scales from a Raspberry PI to a high availability cluster. It requires some additional configuration at the beginning, but it is quite straightforward to work with.

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

In the configuration file, available at `config/[env]/config.edn`, use the following URL to connect to PostgreSQL:

    :DATABASE_URL "postgres://minimily:secret@localhost:5432/minimily"

### Installing JVM

Minimily is open source and free like free beer. You can use Minimily and contribute to it as much as you can. In order to fulfill this promesse, we also need to use open source and free software. In the JVM world, there are free and paid distributions. We want you to be careful about which distribution to pick. So, we would like to recommend the following distributions:

* **OpenJDK**: https://openjdk.java.net/install/
* **Amazon**: https://aws.amazon.com/corretto/

Please, follow the installation instructions in their respective websites.

## Using

## Contributing

### Installing Leiningen

Run from the terminal using the _dev_ profile:

    $ lein run

or using the _prod_ profile:

    $ lein with-profile prod run

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

### Code Conventions

The application is organized in decoupled modules. They are:

- _accounting_: manages the finances of the family
- _documents_: manages digitalized and phisical copies of documents
- _family_: define the family, its members and the relationship between them
- _inventory_: manages all objects in the house
- _auth_: manages the authentification of users

Each module contains two basic folders:

1. _model_: contains the code that access sources of data to add, search, retrieve, modify and delete records
2. _web_: contains the code renders the web user interface to enable all possible data operations

The model is divided in two parts:

1. _sql_: a collection of sql queries organized by entity, keeping them separated from the model logic.
2. _model workspace_: the model that manages the data of the entities.

The web is divided in four parts:

1. _routing_: associates urls with controller functions that return web content.
2. _controller_: functions that receive requests from web clients and interact with the model to generate responses expected by the clients.
3. _ui_:
4. _api_:

### Functions

1. The prefix `get-` indicates the function returns a single value or record.
2. The prefix `find-` indicates the function returns a collection of values or
   records.

## Dependencies

- [HugSQL](https://www.hugsql.org/) is a library to abstract SQL statements as functions.

## License

Copyright © 2017-2018 Hildeberto Mendonca

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
