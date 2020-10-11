package uk.co.probablyfine.inject;

import static java.util.Arrays.stream;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class FemtoInjector {

    private final Map<Class<?>, Class<?>> boundClassToClass = new HashMap<>();
    private final Map<Class<?>, Object> boundClasses = new HashMap<>();

    public <T> T get(Class<T> originalClass) {
        var implementingClass = (Class<T>) implementingClass(originalClass);

        if (!boundClasses.containsKey(implementingClass)) {
            throw new InjectionException("Class [" + originalClass.getName() + "] was not bound");
        }

        if (boundClasses.getOrDefault(implementingClass, null) != null) {
            return originalClass.cast(boundClasses.get(implementingClass));
        }

        try {
            for (Constructor<?> constructor : implementingClass.getConstructors()) {
                var parameterTypes = constructor.getParameterTypes();

                if (!stream(parameterTypes)
                        .map(this::implementingClass)
                        .allMatch(boundClasses::containsKey)) {
                    continue;
                }

                var instance =
                        implementingClass
                                .getConstructor(parameterTypes)
                                .newInstance(
                                        stream(parameterTypes)
                                                .map(this::loadOrCreateInstance)
                                                .toArray());

                boundClasses.put(implementingClass, instance);

                return instance;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new InjectionException(
                "No injectable constructor for [" + originalClass.getName() + "]");
    }

    private Object loadOrCreateInstance(Class<?> klazz) {
        return boundClasses.computeIfAbsent(implementingClass(klazz), this::get);
    }

    public <T> void bind(Class<T> klass) {
        bind(klass, klass);
    }

    public <T> void bind(Class<T> klass, T instance) {
        if (boundClasses.getOrDefault(klass, null) != null) {
            throw new InjectionException("Binding already exists for [" + klass.getName() + "]");
        }

        boundClasses.put(klass, instance);
    }

    public <T, U extends T> void bind(Class<T> original, Class<U> implementation) {
        if (boundClasses.getOrDefault(implementingClass(original), null) != null) {
            throw new InjectionException("Binding already exists for [" + original.getName() + "]");
        }

        boundClassToClass.put(original, implementation);
        boundClasses.put(implementation, null);
    }

    private <T> Class<?> implementingClass(Class<T> originalClass) {
        return boundClassToClass.getOrDefault(originalClass, originalClass);
    }
}
