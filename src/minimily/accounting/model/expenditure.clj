(ns minimily.accounting.model.expenditure)

(defrecord Expenditure [id beneficiary account description amount  ; required
                        payment_date transaction])                 ; optional
