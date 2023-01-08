-- :name active-accounts :? :*
select * from account 
where profile = :profile-id and active is true 
order by name asc

-- :name third-party-accounts :? :*
select * from account where profile = :profile-id and third_party = :third-party-id order by name asc

-- :name inactive-accounts :? :*
select * from account where profile = :profile-id and active is false order by name asc