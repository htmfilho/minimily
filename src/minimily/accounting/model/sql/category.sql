-- :name categories :? :*
select * from transaction_category where profile in (:v*:profile-ids) and parent is null order by name

-- :name categories-children :? :*
select * from transaction_category where profile in (:v*:profile-ids) and parent = :parent-id order by name

-- :name categories-count-children :? :*
select count(*) from transaction_category where profile in (:v*:profile-ids) and parent = :parent-id

-- :name debit-categories :? :*
select * from transaction_category where profile in (:v*:profile-ids) and transaction_type = -1 order by parent, name

-- :name credit-categories :? :*
select * from transaction_category where profile in (:v*:profile-ids) and transaction_type = 1 order by parent, name