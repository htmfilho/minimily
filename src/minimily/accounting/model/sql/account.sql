-- :name accounts-by-family-holders :? :*
select * from account where holder in (:v*:ids) order by balance desc