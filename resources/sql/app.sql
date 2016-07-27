-- :name create-user! :! :n
-- :doc creates a new user record
insert into users
  (name, email, password, updated_at)
values
  (:name, :email, :password, now())

-- :name user-by-token :? :1
-- :doc retrieve a user given the id.
select users.* from users, access_tokens
where
  access_tokens.token = :token and
  access_tokens.expires_at > now() and
  access_tokens.users_id = users.id
