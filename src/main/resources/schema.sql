CREATE EXTENSION if not exists "uuid-ossp";

CREATE SCHEMA if not exists music_metadata;

CREATE TABLE if not exists music_metadata.artist (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    version serial,
    creation_time timestamp without time zone,
    name varchar(255),
    profile_picture_url varchar(255)
);

CREATE TABLE if not exists music_metadata.artist_alias (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    alias varchar(255),
    artist_id uuid REFERENCES music_metadata.artist ON DELETE CASCADE
);

CREATE TABLE if not exists music_metadata.genre (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    version serial,
    creation_time timestamp without time zone,
    name varchar(255)
);

CREATE TABLE if not exists music_metadata.track (
    id uuid default uuid_generate_v4() PRIMARY KEY,
    version serial,
    creation_time timestamp without time zone,
    title varchar(255),
    artist_id uuid REFERENCES music_metadata.artist ON DELETE CASCADE,
    genre_id uuid REFERENCES music_metadata.genre,
    length_seconds serial CHECK (length_seconds > 0),
    album_id uuid
);