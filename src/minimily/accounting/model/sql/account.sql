-- :name accounts-by-holder :? :*
select * from account where holder = :holder

-- :name accounts-by-family-holders :? :*
select * from account where holder in (:v*:ids)