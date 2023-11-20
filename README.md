# McElMusic API

#### Author: Dan McElroy

This application is designed to provide a REST API for a music metadata service that allows users to access and modify
information related to musical artists and their tracks. 

### How to Run

This application should be run in an environment with [Java 17], [Docker] and [Docker Compose] installed.

Once these requirements have been met, ensure that Docker is running and then run `./gradlew bootJar` from
the repository's root directory.

This will deploy a PostgreSQL container, launch the application and seed the database with some sample data
(see [below](#further-documentation)).

### How to Use

All actions are performed via HTTP requests on [http://localhost:8080](http:localhost:8080), and the documentation for 
this API can be found at [http:localhost:8080/docs.html](http:localhost:8080/docs.html) or in YAML format
[here](src/main/resources/public/openapi.yml).

### Sample Data

The database has been pre-seeded with a small amount of data to ease testing (see [previous section](#how-to-use)
for how to get started). The sample data is as follows:

#### Artists
| ID                                   | Name        | Profile Picture URL                                              |
|--------------------------------------|-------------|------------------------------------------------------------------|
| 54a1f1ee-0210-419e-9610-667ecc9e3a5a | Four Tet    | https://i.scdn.co/image/ab6761610000517484e29d09b4917bec2700a0d7 |
| 4020910a-a1d0-4a8c-b949-e1483960b1ea | Larry Heard | https://i.scdn.co/image/29dd50d9a51002524ea2354c9fed0b5ec34b8ae6 |
| 0ca17799-ef55-495b-85e1-a75a1eaf1e52 | Burial      | https://i.scdn.co/image/ab676161000051744be7334b7aed9ca32a732aeb |


#### Artist Aliases
| ID                                   | Artist ID (Name)                                   | Alias                |
|--------------------------------------|----------------------------------------------------|----------------------|
| 0ed74ccf-e369-44de-8828-bc37ff1e2212 | 54a1f1ee-0210-419e-9610-667ecc9e3a5a (Four Tet)    | KH                   |
| f59ccd4e-fc5c-4f62-8964-d5b6e609bfb3 | 54a1f1ee-0210-419e-9610-667ecc9e3a5a (Four Tet)    | ⣎⡇ꉺლ༽இ•̛)ྀ◞ ༎ຶ ༽ৣৢ؞ৢ؞ؖ ꉺლ |
| 5e6b6722-6269-4bae-8064-178c1cfa1479 | 4020910a-a1d0-4a8c-b949-e1483960b1ea (Larry Heard) | Mr. Fingers          |

#### Genres
| ID                                   | Name       |
|--------------------------------------|------------|
| fb1da549-7e39-4286-b5fb-367e875ecb50 | Deep House |
| 99c4dd05-083d-40c5-acaa-e80ffe228a6d | Soul       |

#### Tracks
| ID                                   | Title                                | Genre ID                             | Length (Seconds) | 
|--------------------------------------|--------------------------------------|--------------------------------------|------------------|
| 8f89510a-1c28-44d5-87ad-1ca2121d9374 | Moth                                 | fb1da549-7e39-4286-b5fb-367e875ecb50 | 560              |
| 77851d5a-1f8e-47a3-aa31-ceb5dcb5f261 | The Sun Can't Compare (Long Version) | fb1da549-7e39-4286-b5fb-367e875ecb50 | 467              |
| db756ca1-5b1f-46ac-873a-743ec75afa0b | Scythe Master                        | fb1da549-7e39-4286-b5fb-367e875ecb50 | 480              |

#### Artist Tracks
| Artist ID (Name)                                   | Track ID (Name)                                                             |
|----------------------------------------------------|-----------------------------------------------------------------------------|
| 54a1f1ee-0210-419e-9610-667ecc9e3a5a (Four Tet)    | 8f89510a-1c28-44d5-87ad-1ca2121d9374 (Moth)                                 |
| 54a1f1ee-0210-419e-9610-667ecc9e3a5a (Four Tet)    | db756ca1-5b1f-46ac-873a-743ec75afa0b (Scythe Master)                        |
| 0ca17799-ef55-495b-85e1-a75a1eaf1e52 (Burial)      | 8f89510a-1c28-44d5-87ad-1ca2121d9374 (Moth)                                 |
| 4020910a-a1d0-4a8c-b949-e1483960b1ea (Larry Heard) | 77851d5a-1f8e-47a3-aa31-ceb5dcb5f261 (The Sun Can't Compare (Long Version)) |


### Further Documentation

Further documentation regarding the implementation decisions and assumptions that went into this project can be found
[here](docs/decisions-assumptions.md).