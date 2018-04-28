-- :name family-organizers :? :*
select * from family_member
where organizer is true and family in (select family 
                                       from family_member 
                                       where user_profile = :profile-id)