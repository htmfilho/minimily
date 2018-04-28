-- :name collections-by-profile :? :*
select * from collection 
where profile in (:v*:profile-ids) order by name