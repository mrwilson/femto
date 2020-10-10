package uk.co.probablyfine.inject;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FemtoInjector {

    private final Map<Class<?>, Object> boundClasses = new HashMap<>();

    public <T> T get(Class<T> klass) {
        if (!boundClasses.containsKey(klass)) {
            throw new InjectionException("Class [" + klass.getName() + "] was not bound");
        }

        try {
            for (Constructor<?> constructor : klass.getConstructors()) {
                var parameterTypes = constructor.getParameterTypes();

                if (!Arrays.stream(parameterTypes).allMatch(boundClasses::containsKey)) {
                    continue;
                }

                return klass.getConstructor(parameterTypes)
                        .newInstance(
                                Arrays.stream(parameterTypes)
                                        .map(
                                                klazz ->
                                                        boundClasses.computeIfAbsent(
                                                                klazz, this::get))
                                        .toArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new InjectionException("No injectable constructor for [" + klass.getName() + "]");
    }

    public <T> void bind(Class<T> klass) {
        boundClasses.put(klass, null);
    }

    public <T> void bind(Class<T> klass, T instance) {
        boundClasses.put(klass, instance);
    }
}
