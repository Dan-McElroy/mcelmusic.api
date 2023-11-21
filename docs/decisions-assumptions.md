# Decisions & Assumptions

#### Author: Dan McElroy
#### Last updated: 20th November 2023

### Project Scope

From the provided requirements I decided to product a backend solution only - the requirements make mention of a
homepage, but this could be developed separately as a client of this API.

The API provides a REST interface, with Create, Read, Update and Delete operations for tracks, artists and genres,
.

### Tech Stack

As the only purely technical requirement was to use a JVM language, I settled on a Java Spring Boot WebFlux API,
drawing on my recent experience and my preference for reactive programming.

The backing database is a PostgreSQL instance, primarily managed via Hibernate Reactive with migrations controlled by
Flyway. For local development, this database was deployed as a `postgres` Docker container via Docker Compose .



### Data Access Issues

### Artist of the Day Implementation

The goals of "Artist of the Day" are to provide a consistent Artist result for each request that rotates through
all Artists in the store, once per day.

To achieve this, we take the following steps:
- Find the number of days since the time of the user's request and a fixed date in history
(currently using the "Unix Epoch", aka. January 1st 1970)
- Find the number of artists currently in the database, and use a [modulo](https://en.wikipedia.org/wiki/Modulo)
operation to get a usable index _N_ for our daily Artist (expressed as `daysSinceEpoch % numberOfArtists`)
- Order the artist table by its `creation_time` property (as a non-optional, immutable and easily comparable value,
which has been indexed for optimisation purposes)
- Retrieve the _Nth_ artist from the ordered table.

One slight limitation of this approach from a performance perspective is that it currently uses two separate database
sessions, one to retrieve the number of artists and another to retrieve the Nth artist, but this allows us to keep the
business logic in the service layer of the application and out of the low-level database access code without serious
refactoring.

### Architecture

Throughout development I strove to maintain a clean separation of application layers loosely defined as _User
interface_, _Business logic_ and _Data access_ with data flowing in one direction - ensuring that the details of the
data access layer (primarily made up of `DboRepository` and `Dbo` classes) are not exposed to the business logic layer
(achieved by dependency injection and a second, more abstract layer of `Repository` interfaces). Ideally this would be
verified and preserved by architectural unit tests (see [Next Steps](next-steps.md#expand-automated-testing)).

### API Design

The API is mainly concerned with three resources: `track`, `genre` and `artist`. Each of these resources can be 
interacted with in the following ways:
* `GET /<resource>/<id>`: get one resource with the given ID
* `PATCH /<resource>/<id>`: 

### Outstanding Decisions
- Should Artist of the Day return just artist, or also some songs?
- Should updating aliases of artist be handled differently?
  - Maybe one endpoint for patching Main Name, profile pic etc.
  - And one for Create-Read-Delete on aliases
- Should delete return error for non-existing IDs, or just a different success code?
- Database decision path:
  - Reactive
  - Want to use relational DB
  - Spring Data Reactive solution is J2DBC which on a brief review seemed to not be so great
  - Opted instead for Hibernate

### Data Model

![An Entity-Relationship Diagram of the service's data model](entity-relationship-diagram.svg)

- Genre Objects with IDs
  - Simple right now (just name) but have made create/update DTOs to allow room for it to get more complex in the future
  - (Examples of this?)
- Multiple artists per track (collaborations)
  - If artist deleted and track has no other artists, track deleted
- Stretch goal: Albums (TODO: find more generic term)
  - Album artists = "distinct" collection of song artists in album?
    - But maybe also a primary artist?
  - Track number within album
- Versioned objects to protect against updates out of sync

### Limitations
Unfortunately, at the time of writing the application's functionality is limited due to unresolved issues with the data
access layer. Diagnosing and attempting to resolve this issue took precedence over resolving other important yet
non-critical issues with the design and implementation, described below:
- Setting up testing environments/branching policies
  - Single dev for a very short term project
- Storage solution for profile pictures
- More parameter annotations (mins, maxes, expected String formats)
  - Currently possible to submit a
- Detailed error feedback, i.e. if bad parameters when creating track, which parameters?
  - When creating a track with bad/non-existing artist IDs, bad entries will be ignored
  - Existing error handling feels overengineered, unmaintainable
- Parameter checks and failures in data model
- No current security configuration, app is totally open
- Not very intuitive/dev-friendly setup between domain models and dbos
- No albums
- No soft delete

Ideas for longer-term improvements can be found [here](next-steps.md).