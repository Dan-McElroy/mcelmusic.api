# Next Steps

#### Author: Dan McElroy
#### Last updated: 20th November 2023

At time of writing, the project is mid-development pending some critical issues with the data access layer (see 
[here](decisions-assumptions.md#data-access-issues)) and as such cannot be considered to be "production-ready".
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
The API currently gives users access to interact with tracks, genres and artists. 

### App Functionality
