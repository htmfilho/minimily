(ns minimily.auth.model.user-account)

(defrecord UserAccount [id username password  ; required
                        registration_date])   ; optional
