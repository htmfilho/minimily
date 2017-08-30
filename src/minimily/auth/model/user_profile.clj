(ns minimily.auth.model.user-profile)

(defrecord UserProfile [id user_account first_name last_name  ; required
                        email])                               ; optional
