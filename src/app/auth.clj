(ns app.auth
  (:require
   [app.db.auth :as db]
   [app.db.core :as dbc]
   [buddy.auth :refer [authenticated?]]
   [buddy.auth.accessrules :refer [restrict]]
   [buddy.auth.backends :as backends]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
   [buddy.hashers :as hashers]
   [cheshire.core :as json]
   [clams.conf :as conf]
   [clj-time.coerce :as tc]
   [clj-time.core :as time]))

(declare ^:dynamic *identity*)

(def ^:private json-header "application/json; charset=utf-8")

(defn json-response [data status]
  {:status status
   :headers {"Content-Type" json-header}
   :body (json/encode data)})

(defn on-error [request response]
  (json-response {:error (str "Access to " (:uri request) " is not authorized")}
                 403))

(defn wrap-restricted [handler]
  (restrict handler {:handler authenticated?
                     :on-error on-error}))

(defn wrap-identity [handler]
  (fn [request]
    (binding [*identity* (get-in request [:identity])]
      (handler request))))

(def ^:private base62-chars
  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
(def ^:private base62-size (count base62-chars))

(defn gen-random-token [size]
  (->> (repeatedly size #(rand-int base62-size))
       (map (fn [i] (.charAt base62-chars i)))
       (apply str)))

(defn encrypt-token [tok]
  (when tok
    (hashers/derive tok {:alg :pbkdf2+blake2b-512
                         :salt (conf/get :salt)})))

(defn encrypt-password [password]
  (when password
    (hashers/derive password {:alg :pbkdf2+blake2b-512})))

(defn token->user [token]
  (let [u (dbc/user-by-token {:token (encrypt-token token)})]
    (dissoc u :epassword)))

(defn user-create [name email password]
  (db/users-create {:name name
                    :email email
                    :epassword (encrypt-password password)}))

(defn token-create [user-id token]
  (db/tokens-create {:etoken (encrypt-token token)
                     :users_id user-id
                     :expires_at (tc/to-sql-time (time/plus (time/now)
                                                            (time/years 100)))}))

(defn identity-create
  ([name email password]
   (identity-create name email password (gen-random-token 32)))
  ([name email password token]
   (let [user (user-create name email password)]
     (token-create (:id user) token)
     (token->user token))))

(defn auth-token [request token]
  (token->user token))

(def backend (backends/token {:token-name "Bearer" :authfn #'auth-token}))

(defn wrap-auth [handler]
  (-> handler
      wrap-identity
      (wrap-authentication backend)
      (wrap-authorization backend)))
