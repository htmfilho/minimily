-- :name profile-by-email :? :1
select * from user_profile up where up.email = :email