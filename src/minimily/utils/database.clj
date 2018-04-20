(ns minimily.utils.database
  (:require [hikari-cp.core    :as cp]
            [clojure.string    :as str]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io   :as io]
            [config.core       :refer [env reload-env]]
            [ragtime.jdbc      :as migration]
            [ragtime.repl      :as repl])
  (:import (org.postgresql.util PSQLException)))

(defn copy-file [source-path dest-path]
  (io/copy (io/file source-path) 
           (io/file dest-path)))

(defn decompose-url [url]
  (if-let [url (or url (System/getenv "DATABASE_URL"))]
    (let [double-slashes (str/index-of url "//")
          second-colon   (str/index-of url ":" (+ double-slashes 2))
          at             (str/index-of url "@")
          third-colon    (str/index-of url ":" at)
          slash          (str/index-of url "/" third-colon)]
      {:username      (subs url (+ double-slashes 2) second-colon)
       :password      (subs url (+ second-colon 1) at)
       :server-name   (subs url (+ at 1) third-colon)
       :port-number   (subs url (+ third-colon 1) slash)
       :database-name (subs url (+ slash 1))})
    (do
      (copy-file "config/dev/config.edn.example" "config/dev/config.edn")
      (reload-env)
      (decompose-url (:DATABASE_URL env)))))

(def options {:pool-name "db-pool" 
              :adapter "postgresql" 
              :maximum-pool-size 2})

(defn datasource []
  (let [url (if (nil? env) nil (get env :DATABASE_URL))]
    (make-datasource (conj options (decompose-url url)))))

(defn migrate []
  (repl/migrate {:datastore  (migration/sql-database {:datasource (datasource)})
                 :migrations (migration/load-resources "migrations")}))

(defmacro with-conn [& body]
  `(jdbc/with-db-connection [~'conn {:datasource (datasource)}]
    ~@body))

(defn- valid-id [id]
  (if (int? id)
    id
    (try (Integer/parseInt id)
      (catch Exception e nil))))

(defn find-records [query]
  (with-conn
    (jdbc/query conn query)))

(defn get-record 
  ([table id]
    (with-conn
      (jdbc/get-by-id conn table (valid-id id))))
  ([table id profile-id]
    (first (find-records (str "select * from " (name table) 
                              " where id = " id 
                              " and profile = " profile-id)))))

(defn insert-record
  "Returns a map of fields persisted in the database."
  [table record]
   (with-conn
    (let [record (dissoc record :id)]
      (first (jdbc/insert! conn table record)))))

(defn update-record
  "Returns the number of records updated in the database."
  [table record]
  (with-conn
    (let [id     (:id record)
          record (dissoc record :id)]
      (first (jdbc/update! conn table record ["id = ?" (valid-id id)])))))

(defn save-record
  "If the object doesn't exist it returns the id of the recently persisted 
   object. If the object exists it returns the id that comes with the object 
   only if at least one object is updated or zero if no object is updated."
  [table record]
  (if (nil? (get record :id))
    (:id (insert-record table record))
    (if (> (update-record table record) 0)
      (:id record)
      0)))

(defn delete-record
  "Returns the number of deleted records from the database."
  ([table id]
    (with-conn
      (jdbc/delete! conn table ["id = ?" (valid-id id)])))
  ([table id profile-id]
    (with-conn
      (jdbc/delete! conn table ["id = ? and profile = ?" id profile-id]))))
