# Current Issues

#### Author: Dan McElroy
#### Last updated: 20th November 2023

As described elsewhere in this documentation, development of this project has been regrettably held back by a host of
issues related to the data access layer of the application - specifically, interacting with Hibernate Reactive and JPA.

In this document I'll discuss what led to these issues and the scope of their current impact.

### Decisions That Led to the Issue
As discussed in the [Decisions & Assumptions](decisions-assumptions.md#tech-stack) document, I opted for Spring WebFlux
with reactive controllers as an API framework for this project. Accordingly, it made sense to use a reactive database
framework so as not to lose out on the advantages of reactive programming, or build hacks to make synchronous frameworks
work better with the reactive code.

Separately, it's clear from the requirements of the project that a relational database such as PostgreSQL would be more
suitable than a NoSQL approach such as MongoDB. Unfortunately, my only experience of working with reactive JDK ORMs in
the past have been with MongoDB. With that in mind, I explored my options for working with SQL databases reactively,
and found two main options: [R2DBC](https://r2dbc.io/) and [Hibernate Reactive](https://hibernate.org/reactive/).

Having no experience with either library before, I opted for Hibernate Reactive, as it seemed to have a more intuitive
way of interacting with relationships between objects. However, as my first experience with Hibernate or its reactive
counterpart, a significant portion of the development of this project was spent making mistakes and fixing them instead
of tackling the core issues.

### Impact of the Issue
The main issue being faced right now in the data access layer is related to lazy loading of relations, specifically the
`alias` objects related to a given `artist`. This issue prevents loading of any artist data, which in turn also prevents
loading of track data, and also impacts creation and updating of artists.

Furthermore, I've been unable to work out how to safely delete entities referenced by foreign keys in a non-cascading
way, and this impacts deletion of genres and artists (but not tracks).

I'm certain these issues could be easily resolved if I had more experience and time with Hibernate, but as it stands,
several endpoints of the API return 500 errors even when given valid input.