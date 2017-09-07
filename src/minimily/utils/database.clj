(ns minimily.utils.database
  (:require [hikari-cp.core    :refer :all]
            [clojure.java.jdbc :as jdbc]))

(def options {:pool-name     "db-pool"
              :adapter       "postgresql"
              :username      "minimily_usr"
              :password      "secret"
              :database-name "minimily"
              :server-name   "localhost"
              :port-number   5432})

(def datasource
  (make-datasource options))

(defmacro with-conn [& body]
  `(jdbc/with-db-connection [~'conn {:datasource datasource}]
    ~@body))

(defn find-records [query]
  (with-conn
    (jdbc/query conn query)))

(defn get-record [table id]
  (find-records ["select * from ? where id = ?" table id]))

(defn insert-record
  "Returns the number of inserted records."
  [table record]
   (with-conn
    (let [record (dissoc record :id)]
      (jdbc/insert! conn table (keys record) (vals record)))))

(defn update-record [table record]
  (with-conn
    (let [id     (:id record)
          record (dissoc record :id)]
      (jdbc/update! conn table record ["id = ?" id]))))

(defn save-record [table record]
    (if (nil? (get record :id))
      (insert-record table record)
      (update-record table record)))

(defn delete-record
  "Returns the number of deleted records."
  [table id]
  (with-conn
    (jdbc/delete! conn table ["id = ?" id])))
