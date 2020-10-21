# Kotlin Starter
A Kotlin quick start project with unit tests.
This code has been built and tested on `JDK 1.8.0_181`.

#### Run Program using Gradle
```shell script
$ ./gradlew run
> Task :run
Hello!
```

#### Run Program using Jar
```shell script
$ ./gradlew clean build    ### First build the jar
```

```shell script
$ java -jar build/libs/kotlin-threads-1.0-SNAPSHOT.jar 
Hello!
```

#### Run Tests
```shell script
$ ./gradlew test
> Task :test
com.starter.AppTest > 2 + 3 equals 5 PASSED
Results: SUCCESS (1 tests, 1 passed, 0 failed, 0 skipped)
```
