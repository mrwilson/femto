# femto

![Maven Central](https://img.shields.io/maven-central/v/uk.co.probablyfine/femto?style=flat-square)

Femto is a teeny-tiny [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) library written in pure standard Java.

## Installing

Maven:
```xml
<dependency>
  <groupId>uk.co.probablyfine</groupId>
  <artifactId>femto</artifactId>
  <version>${version}</version>
</dependency>
```

Gradle:
```groovy
implementation "uk.co.probablyfine:femto:${version}"
```

## Usage 

```java
// Initialise a new injector
var injector = new FemtoInjector();

// Bind to a class (searches for a valid constructor)
injector.bind(One.class); 

// Bind a class to an instance
injector.bind(Two.class, new TwoImplementation()); 

// Depends on One, Two
injector.bind(Three.class); 

// Create a new object
Three three = injector.get(Three.class);
```

## Design Decisions

- **Singleton by default**: The first call to `FemtoInjector#get` caches the result for each class and returns the same value afterwards
- **Lazy by default**: `FemtoInjector` does not initialise any classes until `.get()` is called.
- **No annotation "magic"**: Clients register components directly with the injector rather than using JSR 330 (`@Inject`) annotations.
- **Prefer standard Java over dependencies**: Keeps the library small, and the source code easy to read.