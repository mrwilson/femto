package uk.co.probablyfine.inject;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class FemtoInjectorTest {

    @Test
    void returnsNullForUnboundClass() {
        var injector = new FemtoInjector();
        EmptyClass klass = injector.get(EmptyClass.class);

        assertThat(klass, nullValue());
    }

    @Test
    void returnsBoundClass_WithPublicZeroArgumentConstructor() {
        var injector = new FemtoInjector();

        injector.bind(EmptyConstructorClass.class);

        assertThat(injector.get(EmptyConstructorClass.class), notNullValue());
    }

    static class EmptyClass {}

    static class EmptyConstructorClass {
        public EmptyConstructorClass() {}
    }
}
