-- :name family-of-organizer :? :*
select f.* 
from family f left join family_member fm on fm.family = f.id 
where fm.organizer is true and fm.user_profile = :profile-id