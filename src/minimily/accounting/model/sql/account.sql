-- :name active-accounts :? :*
select * from account where holder in (:v*:ids) and active is true order by name desc

-- :name inactive-accounts :? :*
select * from account where holder in (:v*:ids) and active is false order by name desc