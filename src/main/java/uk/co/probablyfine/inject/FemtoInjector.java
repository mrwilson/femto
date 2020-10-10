package uk.co.probablyfine.inject;

import java.util.ArrayList;
import java.util.List;

public class FemtoInjector {

    private final List<Class<?>> boundClasses = new ArrayList<>();

    public <T> T get(Class<T> klass) {
        if (!boundClasses.contains(klass)) {
            return null;
        }

        try {
            return klass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void bind(Class<T> klass) {
        boundClasses.add(klass);
    }
}
