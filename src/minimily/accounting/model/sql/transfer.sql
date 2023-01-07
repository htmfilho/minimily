-- :name transfers-from-profile :? :*
select * from account_transfer where profile_from = :profile-id order by date_created asc

-- :name transfers-to-profile :? :*
select * from account_transfer where profile_to = :profile-id order by date_created asc