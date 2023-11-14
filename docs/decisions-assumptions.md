# Decisions & Assumptions

### Project Scope
- Backend only
- Provide public API endpoints for required operations (adding tracks, editing artist name, Artist of the Day etc)
- Provide additional endpoints for other necessary admin functions (adding artists, editing/deleting tracks)

### Tech Stack
- Spring Boot application providing a REST API
- Backed by a PostgreSQL DB

### Limitations
- Setting up testing environments/branching policies
  - Single dev for a very short term project
- Storage solution for profile pictures
- More parameter annotations (mins, maxes, expected String formats)

### Architecture
- Onion!
  - Dependencies flowing inward, inner layers having no knowledge of outer layers
- API
  - /track PUT

### Data Model
- Genre Objects with IDs
- Multiple artists per track (collaborations)
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