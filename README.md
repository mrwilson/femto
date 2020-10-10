# femto-di-java

FemtoDI, a teeny-tiny dependency injection library.

```java
var injector = new FemtoInjector();

injector.bind(One.class); 
injector.bind(Two.class); // Depends on One

Two two = injector.get(Two.class);
```