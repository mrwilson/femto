# femto-di

FemtoDI, a teeny-tiny [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) library.

```java
var injector = new FemtoInjector();

injector.bind(One.class); 
injector.bind(Two.class); // Depends on One

Two two = injector.get(Two.class);
```

## Design Decisions

- **Singleton by default**: The first call to `FemtoInjector#get` caches the result for each class and returns the same value afterwards
- **Lazy by default**: `FemtoInjector` does not initialise any classes until `.get()` is called.
- **No annotation "magic"**: Clients register components directly with the injector rather than using JSR 330 (`@Inject`) annotations.