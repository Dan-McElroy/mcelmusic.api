CREATE EXTENSION if not exists "uuid-ossp";

INSERT INTO artist (id, name, profile_picture_url)
VALUES ('54a1f1ee-0210-419e-9610-667ecc9e3a5a', 'Four Tet', 'https://i.scdn.co/image/ab6761610000517484e29d09b4917bec2700a0d7');

INSERT INTO artist (id, name, profile_picture_url)
VALUES ('4020910a-a1d0-4a8c-b949-e1483960b1ea', 'Larry Heard', 'https://i.scdn.co/image/29dd50d9a51002524ea2354c9fed0b5ec34b8ae6');

INSERT INTO artist (id, name, profile_picture_url)
VALUES ('0ca17799-ef55-495b-85e1-a75a1eaf1e52', 'Burial', 'https://i.scdn.co/image/ab676161000051744be7334b7aed9ca32a732aeb');

INSERT INTO artist_alias (id, artist_id, alias)
VALUES ('0ed74ccf-e369-44de-8828-bc37ff1e2212', '54a1f1ee-0210-419e-9610-667ecc9e3a5a', 'KH');

INSERT INTO artist_alias (id, artist_id, alias)
VALUES ('f59ccd4e-fc5c-4f62-8964-d5b6e609bfb3', '54a1f1ee-0210-419e-9610-667ecc9e3a5a', '⣎⡇ꉺლ༽இ•̛)ྀ◞ ༎ຶ ༽ৣৢ؞ৢ؞ؖ ꉺლ');

INSERT INTO artist_alias (id, artist_id, alias)
VALUES ('5e6b6722-6269-4bae-8064-178c1cfa1479', '4020910a-a1d0-4a8c-b949-e1483960b1ea', 'Mr. Fingers');

INSERT INTO genre (id, name)
VALUES ('fb1da549-7e39-4286-b5fb-367e875ecb50', 'Deep House');

INSERT INTO genre (id, name)
VALUES ('99c4dd05-083d-40c5-acaa-e80ffe228a6d', 'Soul');

INSERT INTO track (id, length_seconds, genre_id, title)
VALUES ('8f89510a-1c28-44d5-87ad-1ca2121d9374', 560, 'fb1da549-7e39-4286-b5fb-367e875ecb50', 'Moth');

INSERT INTO track (id, length_seconds, genre_id, title)
VALUES ('77851d5a-1f8e-47a3-aa31-ceb5dcb5f261', 467, 'fb1da549-7e39-4286-b5fb-367e875ecb50', 'The Sun Can''t Compare (Long Version)');

INSERT INTO track (id, length_seconds, genre_id, title)
VALUES ('db756ca1-5b1f-46ac-873a-743ec75afa0b', 480, 'fb1da549-7e39-4286-b5fb-367e875ecb50', 'Scythe Master');

INSERT INTO artist_tracks (artist_id, track_id) -- Four Tet > Moth
VALUES ('54a1f1ee-0210-419e-9610-667ecc9e3a5a', '8f89510a-1c28-44d5-87ad-1ca2121d9374');

INSERT INTO artist_tracks (artist_id, track_id) -- Four Tet > Scythe Master
VALUES ('54a1f1ee-0210-419e-9610-667ecc9e3a5a', 'db756ca1-5b1f-46ac-873a-743ec75afa0b');

INSERT INTO artist_tracks (artist_id, track_id) -- Burial > Moth
VALUES ('0ca17799-ef55-495b-85e1-a75a1eaf1e52', '8f89510a-1c28-44d5-87ad-1ca2121d9374');

INSERT INTO artist_tracks (artist_id, track_id) -- Larry Heard > The Sun Can't Compare
VALUES ('4020910a-a1d0-4a8c-b949-e1483960b1ea', '77851d5a-1f8e-47a3-aa31-ceb5dcb5f261');