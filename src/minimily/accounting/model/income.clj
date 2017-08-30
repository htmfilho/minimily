(ns minimily.accounting.model.income)

(defrecord Income [id sponsor account description amount  ; required
                   payment_date transaction])             ; optional
