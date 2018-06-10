-- :name authenticated-user-profile :? :1
select p.id as id, first_name, last_name 
from user_account u left join user_profile p on u.id = p.user_account
where u.username = :username and u.password = :password