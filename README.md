# lenses-udf-example
Example for User Defined Functions for Lenses SQL engine

# Compile,Build, Package
This project is using the Maven build system. So to build you should run:

```bash
mvn clean package
```

# Lenses UDF

User-Defined Functions (aka UDF) is a feature of Lenses SQL that allows you to define
new functions that extend the existing vocabulary of Lenses SQL’s DSL for manipulating data.
Many functions are already built in into Lenses SQL and over time that set is expected to grow.
However, a user might still need a function that is not covered by the default package.
Lenses provides a simple API that can be implemented to provide custom functions that go beyond the built-in ones.

# Try it out with Lenses box

```
docker run -e ADV_HOST=127.0.0.1 -e EULA='<put your EULA here>' --rm -p 3030:3030 -p 9092:9092 -v $(pwd)/target/lenses-udf-example-1.0.0.jar:/plugins/udf.jar lensesio/box:latest
```
