# GitHub Caching Service
This is a basic service which acts as a caching layer on top of the GitHub API. By default it periodically retrieves
and caches (using a [Redis](https://redis.io/) cache) the GitHub API endpoints:
- `/`
- `/orgs/lightbend`
- `/orgs/lightbend/members`
- `/orgs/lightbend/repos`

All other endpoints from the GitHub API are proxied and once retrieved once cached until evicted.

## Running the service
The service requires a couple dependencies to run:
- A [Redis](https://redis.io/) server running on `localhost` and the default port `6379` (this is theoretically configurable, but has only 
ever been tested with these parameters). 
During testing I installed and ran Redis using instructions in the [Redis Quickstart](https://redis.io/topics/quickstart). 
- An evironment variable called `GITHUB_API_TOKEN` containing a (unsurprisingly) GitHub API token. To set this environment
variable (assuming a Unix system) use `export GITHUB_API_TOKEN={token}`.

Once these requirements are met to start the service use one of the two methods below:
### Using the Executable JAR (***Easiest choice if Maven not installed***)
For convenience I've checked the executable jar into the root of the repo [here](github-cache-0.0.1-SNAPSHOT.jar) (which is
obviously not what would normally be done but I wasn't sure if people would have/want Maven installed).
To run it navigate to the project's location and run
```
java -jar github-cache-0.0.1-SNAPSHOT.jar --server.port={port}
```

### Using Maven
If you have Maven installed or are okay with Maven being installed then navigate to the root of the project and run the command
```
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port={port}
```
where `port` is the port to run the service on. Note that this command will search for a Maven installation
on your path and if not found will install one in a default location so might take some time to run the first
time. If you'd like to just build the project see the final section on Building the Jar

## Testing the Service
The service exposes several endpoints. If you happen to use  [Insomnia](https://insomnia.rest/) as an API client then
you can import the configured requests using [the Insomnia configuration file](GitHubCache-Insomnia-Config.json). Also
included is [a HAR file](GitHubCache-Config.har) which could maybe be imported into your API client of choice (Postman
doesn't seem to support it still though). If neither of those work well for you then the list of requests are given below:
- Healthcheck
```
GET /healthcheck
```

- Top-N Lightbend repos by forks
```
GET /view/top/{N}/forks
```

- Top-N Lightbend repos by last updated time
```
GET /view/top/{N}/last_updated
```

- Top-N Lightbend repos by open issues
```
GET /view/top/{N}/open_issues
```

- Top-N Lightbend repos by stars
```
GET /view/top/{N}/stars
```

- Top-N Lightbend repos by watchers (see the [Known Issues](#known-issues) section for a discussion on why this 
doesn't really work)
```
GET /view/top/{N}/watchers
```

- Proxy request to GitHub or get from cache
```
GET /{any valid GitHub request}
```
For example, `/orgs/parse-community/members` will proxy the request to the GitHub API (then cache for a time) 

## TODOs
- Unit tests
- Cache compression
- More robust error handling
- Refactor to allow for more reactive practices (use `WebClient` as it was intended)
- Security
- For use in production definitely need more robust Redis info/configuration

## Known Issues
- The GitHub repos endpoint no longer returns `subscribed_count` which is the number of watchers. Instead this
value comes from the subscribers endpoint (based on some Googling). As such for the TopN-Watchers we return the top values for
the field `watchers_count`, but this is really number of stars now so no value returns that's the number
of watchers of a repo.

## Building the jar and running (if desired)
Build the project using maven. In particular, if you navigate to the root directory of the project and
run the command `mvn clean package` there will be an executable jar file created in the target directory
called `github-cache-0.0.1-SNAPSHOT.jar`. To run this jar use the command
```
java -jar target/github-cache-0.0.1-SNAPSHOT.jar --server.port={port}
```
where `port` is the port to start the service on.
