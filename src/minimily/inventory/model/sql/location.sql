-- :name locations-by-profile :? :*
select * from location 
where profile in (:v*:profile-ids) order by name