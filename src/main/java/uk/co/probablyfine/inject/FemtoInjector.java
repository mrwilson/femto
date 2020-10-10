package uk.co.probablyfine.inject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FemtoInjector {

    private final List<Class<?>> boundClasses = new ArrayList<>();

    public <T> T get(Class<T> klass) {
        if (!boundClasses.contains(klass)) {
            return null;
        }

        try {
            var parameterTypes = klass.getConstructors()[0].getParameterTypes();

            return klass.getConstructor(parameterTypes).newInstance(
                Arrays.stream(parameterTypes).map(this::get).toArray()
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> void bind(Class<T> klass) {
        boundClasses.add(klass);
    }
}
