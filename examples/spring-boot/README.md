# Demo Application demonstrating ProGuard applied to a Spring Boot application

## Building

```
./gradlew clean proguard --info
```

Spring Boot applications contain a BOOT-INF folder which contains the application class files and library jars.
We must first extract the program classes, then apply ProGuard to them and finally repackage the application.

## Executing

The unobfuscated application will be located at `build/libs/demo-0.0.1.jar` and the
obfuscated application will be located at `build/libs/demo-0.0.1-obfuscated.jar`.

They can be executed as follows:

```
java -jar build/libs/demo-0.0.1.jar
```

or

```
java -jar build/libs/demo-0.0.1-obfuscated.jar
```

