-- :name goods-by-location :? :*
select * from good 
where profile = :profile-id and 
      location = :location-id
order by name

-- :name goods-by-collection :? :*
select * from good 
where profile = :profile-id and 
      collection = :collection-id
order by name

-- :name good :? :*
select g.id, g.name, g.description, g.quantity, g.value, 
       l.id as location_id, l.name as location, 
       c.id as collection_id, c.name as collection 
from good g left join location l on g.location = l.id
            left join collection c on g.collection = c.id
where g.id = :id and 
      g.profile = :profile-id