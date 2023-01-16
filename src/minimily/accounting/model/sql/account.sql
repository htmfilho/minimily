-- :name active-accounts :? :*
select * from account 
where profile = :profile-id and active is true and third_party is null
order by name asc

-- :name inactive-accounts :? :*
select * from account 
where profile = :profile-id and active is false and third_party is null
order by name asc

-- :name all-third-party-accounts :? :*
select * from account where profile = :profile-id and third_party is not null order by name asc

-- :name specific-third-party-accounts :? :*
select * from account where profile = :profile-id and third_party = :third-party-id order by name asc