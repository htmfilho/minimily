-- :name authenticated-user-profile :? :1
select p.id as id, first_name, last_name 
from user_account u left join user_profile p on u.id = p.user_account
where u.username = :username

-- :name find-by-username ? :1
select * from user_account where username = :username

-- :name find-by-verification ? :1
select p.id as id, first_name, last_name 
from user_account u left join user_profile p on u.id = p.user_account 
where u.verification = :verification