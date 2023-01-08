-- :name categories :? :*
select * from transaction_category where profile = :profile-id and parent is null order by name

-- :name categories-children :? :*
select * from transaction_category where profile = :profile-id and parent = :parent-id order by name

-- :name categories-count-children :? :*
select count(*) from transaction_category where profile = :profile-id and parent = :parent-id

-- :name debit-categories :? :*
select * from transaction_category where profile = :profile-id and transaction_type = -1 order by name

-- :name credit-categories :? :*
select * from transaction_category where profile = :profile-id and transaction_type = 1 order by name