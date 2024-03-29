-- :name transactions-by-profile-account :? :*
select * from transaction 
where profile = :profile-id and account = :account-id 
order by date_transaction desc

-- :name transactions-by-account :? :*
select type, amount from transaction where account = :account-id

-- :name transactions-balance-history :? :*
select description, amount, date_transaction, type
from transaction 
where account = :account-id
order by date_transaction asc

-- :name transactions-category :? :*
select * from transaction 
where category = :category-id
order by date_transaction desc