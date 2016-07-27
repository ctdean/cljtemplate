(ns app.db.auth
  "@ctdean"
  (:require
   [app.db.core]                        ; Make sure the db connection loads
   [common.db.ops :as ops]))

(ops/define-crud-ops :users "users-")
(ops/define-crud-ops :access_tokens "tokens-")
