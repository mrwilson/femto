package uk.co.probablyfine.inject;

import static java.util.Arrays.stream;

import java.lang.reflect.Constructor;
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

                if (!stream(parameterTypes).allMatch(boundClasses::containsKey)) {
                    continue;
                }

                return klass.getConstructor(parameterTypes)
                        .newInstance(
                                stream(parameterTypes).map(this::loadOrCreateInstance).toArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new InjectionException("No injectable constructor for [" + klass.getName() + "]");
    }

    private Object loadOrCreateInstance(Class<?> klazz) {
        return boundClasses.computeIfAbsent(klazz, this::get);
    }

    public <T> void bind(Class<T> klass) {
        boundClasses.put(klass, null);
    }

    public <T> void bind(Class<T> klass, T instance) {
        boundClasses.put(klass, instance);
    }
}
