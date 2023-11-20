# Decisions & Assumptions

### Project Scope

From the provided requirements I decided to product a backend solution only - the requirements make mention of a
homepage, but this could be developed separately as a client of this API.

The API provides a REST interface, with Create, Read, Update and Delete operations for tracks, artists and genres.

### Tech Stack

As the only purely technical requirement was to use a JVM language, I settled on a Java Spring Boot WebFlux API,
drawing on my recent experience and my preference for reactive programming.

The backing database is a PostgreSQL instance, primarily managed via Hibernate Reactive with migrations controlled by
Flyway. For local development, this database was deployed as a `postgres` Docker container via Docker Compose .

### Limitations
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
- Onion!
  - Dependencies flowing inward, inner layers having no knowledge of outer layers
- API
  - /track PUT

### Data Model

![An Entity-Relationship Diagram of the service's data model](docs%2Fentity-relationship-diagram.svg)

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

### Outstanding Decisions
- Should Artist of the Day return just artist, or also some songs?
- Should updating aliases of artist be handled differently?
  - Maybe one endpoint for patching Main Name, profile pic etc.
  - And one for Create-Read-Delete on aliases
- Should delete return error for non-existing IDs, or just a different success code?