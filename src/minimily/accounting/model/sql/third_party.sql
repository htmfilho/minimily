-- :name third-parties :? :*
select * from third_party where profile = :profile-id order by name asc

-- :name other-third-parties :? :*
select * from third_party 
where id not in (select coalesce(third_party, 0) from account where id = :account-id and profile = :profile-id)
      and profile = :profile-id
order by name asc