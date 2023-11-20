# Decisions & Assumptions

### Project Scope
- Backend only
- Provide public API endpoints for required operations (adding tracks, editing artist name, Artist of the Day etc)
- Provide additional endpoints for other necessary admin functions (adding artists, editing/deleting tracks)

### Tech Stack
- Spring Boot Webflux application providing a REST API
- Backed by a PostgresQL DB via Hibernate Reactive with Flyway migrations

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

### Artist of the Day Implementation
- Index on/order by artist creation time
- Get days since Instant EPOCH, mod by number of artist records and retrieve 1 artist at that index
- Implemented with two separate sessions, one per query (count and then select/order-by), which isn't ideal but also it
made sense to keep logic in service
  - Also count() could potentially be re-used in future

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