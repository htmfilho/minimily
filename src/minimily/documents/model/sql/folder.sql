-- :name folders-by-profile :? :*
select * from folder where profile = :profile-id and parent is null

-- :name folders-children :? :*
select * from folder where profile = :profile-id and parent = :parent-id

-- :name folders-count-children :? :*
select count(*) from folder where profile = :profile-id and parent = :parent-id