package uk.co.probablyfine.inject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FemtoInjector {

    private final Map<Class<?>, Object> boundClasses = new HashMap<>();

    static class InjectionException extends RuntimeException {
        InjectionException(String message) {
            super(message);
        }
    }

    public <T> T get(Class<T> klass) {
        if (!boundClasses.containsKey(klass)) {
            throw new InjectionException("Class [" + klass.getName() + "] was not bound");
        }

        try {
            var parameterTypes = klass.getConstructors()[0].getParameterTypes();

            return klass.getConstructor(parameterTypes)
                    .newInstance(
                            Arrays.stream(parameterTypes)
                                    .map(klazz -> boundClasses.computeIfAbsent(klazz, this::get))
                                    .toArray());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void bind(Class<T> klass) {
        boundClasses.put(klass, null);
    }

    public <T> void bind(Class<T> klass, T instance) {
        boundClasses.put(klass, instance);
    }
}
