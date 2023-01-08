-- :name collections-by-profile :? :*
select * from collection 
where profile = :profile-id order by name