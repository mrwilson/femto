package uk.co.probablyfine.inject;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

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

    @Test
    void returnsBoundClass_WithSingleDependency() {
        var injector = new FemtoInjector();

        injector.bind(SingleDependencyClass.class);
        injector.bind(SingleDependencyClass.Inner.class);

        var instance = injector.get(SingleDependencyClass.class);

        assertThat(instance.say(), is("Hello, World!"));
    }
}
