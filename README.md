[![Build Java CI with Maven](https://github.com/telcirion/odin/workflows/Build%20Java%20CI%20with%20Maven/badge.svg)](https://github.com/telcirion/odin/actions?query=workflow%3A%22Build+Java+CI+with+Maven%22)
[![Code QL](https://github.com/telcirion/odin/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/telcirion/odin/actions/workflows/codeql-analysis.yml/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=telcirion_odin&metric=alert_status)](https://sonarcloud.io/dashboard?id=telcirion_odin)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=telcirion_odin&metric=coverage)](https://sonarcloud.io/dashboard?id=telcirion_odin)
# ODIN-Framework
A framework for DDD and EDA.
## Introduction
This framework is based on the ideas that a "domain" can be modeled not only as a static data model but also as a series of "Events" and "Commands" that reflects behaviour. The concepts presented here are originating from the Domain Driven Design (DDD) community, so the credits are for them. Use your favorite search engine for finding out more about this ;-)

## What does this framework do?
As stated in the introduction, the basic building blocks for modeling are events and commands. Events are things
that have happened and are irreversible facts. Events are caught by event handlers or process managers, which can decide
to send a command to a command handler which targets a specific aggregate (data within a certain consistency boundary
within the domain.)

This framework facilitates the above pattern by providing the presented concepts with interfaces, implementation and
some glue to tie it all together.

## What's in a name
Every framework needs a name. Something cool, mythical and easy to remember. There are probably way more projects that
share this name, tough luck. Anyway, to quote [wikipedia](https://en.wikipedia.org/wiki/Norse_mythology): "Odin pursues knowledge throughout the worlds",
which seems to fit nicely with the DDD concepts.

## How to use
See example in [test](https://github.com/telcirion/odin/tree/master/example/src/test/java/odin/example/applicationservices).

## Project structure
- **`framework\src\main\java\odin\concepts\`**, this is where the core DDD concepts are postulated as interfaces, the sub-packages are layed out conforming to the ["Onion model"](https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/)
- **`framework\src\main\java\odin\framework\`**, some supporting abstract classes implementing interfaces that can be used to avoid logic duplication.
- **`framework\src\main\java\odin\infrastructure\`**, some supporting classes which implement the interfaces requiring external infrastructural libraries.
- **`example\src\test\java\odin\example\test\`**, a basic scenario test which demonstrates this framework.
