## Development

The plugin is developed using the Micronaut framework. The following sections describe how to build and run the application.

### Running the application

To run the application, use the following command from the root project:

```bash
> ./gradlew morpheus-plugin-site:run
```
That should work, but since it currently doesn't, execute the `buildRun.sh` script in this project's directory.

```bash
> ./buildRun.sh
```

Alternatively, can run the application using gradle in the `morpheus-plugin-site` directory:

```bash
> sdk use gradle 8.5
> gradle run
```
_**NOTE:**  java 17 and gradle 8.5+ are required to run the application._

### Running the tests

To run the tests, use the following command from the root project:

```bash
> ./gradlew morpheus-plugin-site:test
```

### Reference Documentation

- [Micronaut Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

