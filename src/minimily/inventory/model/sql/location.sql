-- :name locations-by-profile :? :*
select * from location 
where profile = :profile-id order by name