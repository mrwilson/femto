package uk.co.probablyfine.inject;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

class FemtoInjectorTest {

    @Test
    void returnsNullForUnboundClass() {
        var injector = new FemtoInjector();
        EmptyClass klass = injector.get(EmptyClass.class);

        assertThat(klass, nullValue());
    }

    static class EmptyClass {}
}
