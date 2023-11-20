CREATE EXTENSION if not exists "uuid-ossp";

CREATE TABLE if not exists artist (
    id uuid default uuid_generate_v4() primary key,
    version integer default 1,
    creation_time timestamp without time zone default now(),
    name varchar(255),
    profile_picture_url varchar(255)
);

CREATE TABLE if not exists artist_alias (
    id uuid default uuid_generate_v4() primary key,
    alias varchar(255),
    artist_id uuid not null,
    foreign key (artist_id) references artist (id)
        match simple on update no action on delete cascade
);

CREATE TABLE if not exists genre (
    id uuid default uuid_generate_v4() primary key,
    version integer default 1,
    creation_time timestamp without time zone default now(),
    name varchar(255)
);

CREATE TABLE if not exists track (
    id uuid default uuid_generate_v4() primary key,
    version integer default 1,
    creation_time timestamp without time zone default now(),
    title varchar(255),
    genre_id uuid,
    length_seconds integer check (length_seconds > 0),
    foreign key (genre_id) references genre (id)
        match simple on update no action on delete no action
);

CREATE TABLE if not exists artist_tracks (
    artist_id uuid not null,
    track_id uuid not null,
    primary key (artist_id, track_id),
    foreign key (artist_id) references artist (id)
        match simple on update no action on delete no action,
    foreign key (track_id) references track (id)
        match simple on update no action on delete no action
);

CREATE INDEX creation_time_idx ON artist using btree (creation_time);

