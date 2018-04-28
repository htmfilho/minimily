-- :name documents-by-folder :? :*
select * from document where folder = :folder-id

-- :name documents-count-in-folder :? :*
select count(*) from document where folder = :folder-id