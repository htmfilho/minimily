-- :name folders-by-profile :? :*
select * from folder where profile in (:v*:profile-ids) and parent is null

-- :name folders-children :? :*
select * from folder where profile in (:v*:profile-ids) and parent = :parent-id

-- :name folders-count-children :? :*
select count(*) from folder where profile in (:v*:profile-ids) and parent = :parent-id