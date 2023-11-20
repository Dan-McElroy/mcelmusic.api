CREATE EXTENSION if not exists "uuid-ossp";

CREATE TABLE if not exists artist (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    version serial,
    creation_time timestamp without time zone,
    name varchar(255),
    profile_picture_url varchar(255)
);

CREATE TABLE if not exists artist_alias (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    alias varchar(255),
    artist_id uuid REFERENCES artist (id) ON DELETE CASCADE
);

CREATE TABLE if not exists genre (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    version serial,
    creation_time timestamp without time zone,
    name varchar(255)
);

CREATE TABLE if not exists track (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    version serial,
    creation_time timestamp without time zone,
    title varchar(255),
    genre_id uuid REFERENCES genre (id) ON UPDATE CASCADE,
    length_seconds serial CHECK (length_seconds > 0)
);

CREATE TABLE if not exists artist_tracks (
    track_id uuid REFERENCES track (id) ON UPDATE CASCADE,
    artist_id uuid REFERENCES artist (id) ON UPDATE CASCADE
);

CREATE INDEX creation_time_idx ON artist (creation_time)