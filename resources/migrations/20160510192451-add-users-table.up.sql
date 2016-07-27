/**
 * UP
 */

CREATE TABLE users (
    id                  serial primary key,
    name                text NOT NULL,
    email               text NOT NULL UNIQUE,
    epassword           text NOT NULL,

    created_at          timestamp without time zone DEFAULT now() NOT NULL,
    updated_at          timestamp without time zone DEFAULT now() NOT NULL,
    deleted_at          timestamp without time zone
);

ALTER SEQUENCE users_id_seq RESTART WITH 1000;
CREATE INDEX users_deleted_at_index ON users (deleted_at);

CREATE TABLE access_tokens (
    id                  serial primary key,
    etoken              text NOT NULL UNIQUE,
    users_id            integer NOT NULL,
    expires_at          timestamp without time zone NOT NULL,

    created_at          timestamp without time zone DEFAULT now() NOT NULL,
    updated_at          timestamp without time zone DEFAULT now() NOT NULL
);

ALTER SEQUENCE access_tokens_id_seq RESTART WITH 1000;
