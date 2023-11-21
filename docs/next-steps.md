# Next Steps

#### Author: Dan McElroy
#### Last updated: 20th November 2023

At time of writing, the project is mid-development pending some critical issues with the data access layer (see 
[here](current-issues.md) for details) and as such cannot be considered to be "production-ready".
With that in mind, here are the next steps I would consider to prepare this application for use in production 
(**assuming that existing issues with data access have been resolved**):

### Expand Automated Testing
At the moment, the application is only supported by unit tests, and the database repository are not covered. Next steps
in this area would be as follows:
- Expand unit testing to cover repository classes.
- Develop integration tests to ensure correct behaviour when the project runs as a whole, from HTTP controllers to
data repositories.
- Add architectural unit tests ensure the integrity of the [architectural design](decisions-assumptions.md#architecture)
using a library like [ArchUnit](https://github.com/TNG/ArchUnit).

### Add CI/CD
In its current state, the app has a simple GitHub Action which builds and publishes a Docker container to Docker Hub.
To expand upon this, I would aim to:
- Improve the configuration of the app to be more customisable depending on environment.
- Reduce duplication of database config (currently duplicated in three separate config files) and move to secrets to be
specified per-environment.
- Set up a cloud container service such as Amazon ECS or Azure Container Apps and create a GitHub Action to deploy to
said service on every merge to a `release/*` branch.
  - With more time and resources, set up an identical staging environment which is deployed to on every merge to the 
  `master` branch.

### Add Authentication & Authorization
Currently, the application is not secured, so every user is freely able to manipulate all music metadata. I would make
the following first steps to avoid this:

- Add OAuth2 security with Google for all endpoints to reduce the risk of unwanted traffic (bots etc.)
- Configure additional scopes for specific users to secure access to PUT, PATCH and DELETE endpoints
- Provide a Postman collection in the repository with all endpoints and access token generation to assist client
developers in testing the API

### Reworking the API
The API currently gives users identical ways to access functionality for tracks, genres and artists (with the exception
of Artist of the Day). This can lead to a slightly clunky integration process for any integrating client, particularly
around reading and updating data. I would add the following additional endpoints:
- `/v1/artist/<id>/aliases`
  - `GET`: get a list of artist aliases, separate from the base Artist object
  - `PUT`: add an alias to an artist
  - `DELETE` (with `name` as query parameter): remove an alias from an artist by name
- `/v1/artist/<id>/tracks`
  - `GET`: return a list of tracks by the artist. It would be fairly important to add a pagination parameter like 
  `offset` here to keep request sizes under control, and it could also be extended with other standard query parameters
  like filtering and ordering.
- `v1/genre/<id>/tracks`
  - `GET`: see the above details for `artist/<id>/tracks`, but returning all the tracks belonging to a particular genre.
- `v1/track/<id>/artists`
  - `GET`: get a list of artists responsible for a particular track, separate from the full Track object.
  - `PUT`: add a contributing artist to a track.
- `v1/track/<id>artists/<artist_id>`
  - `DELETE`: remove a contributing artist from a track.