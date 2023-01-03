-- :name third-parties :? :*
select * from third_party order by name asc

-- :name other-third-parties :? :*
select * from third_party 
where id not in (select coalesce(third_party, 0) from account where id = :account-id)
order by name asc