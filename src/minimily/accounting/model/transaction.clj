(ns minimily.accounting.model.transaction)

(defrecord Transaction [id account type amount          ; required
                        description date_transaction])  ; optional
