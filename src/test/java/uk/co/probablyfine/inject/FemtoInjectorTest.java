package uk.co.probablyfine.inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FemtoInjectorTest {

    private final FemtoInjector injector = new FemtoInjector();

    @Test
    void returnsNullForUnboundClass() {
        var exception =
                assertThrows(InjectionException.class, () -> injector.get(EmptyClass.class));

        assertThat(
                exception.getMessage(),
                is("Class [uk.co.probablyfine.inject.EmptyClass] was not bound"));
    }

    @Test
    void returnsBoundClass_WithPublicZeroArgumentConstructor() {
        injector.bind(EmptyConstructorClass.class);

        assertThat(injector.get(EmptyConstructorClass.class), notNullValue());
    }

    @Test
    void returnsBoundClass_WithSingleDependency() {
        injector.bind(SingleDependencyClass.class);
        injector.bind(SingleDependencyClass.Inner.class);

        var instance = injector.get(SingleDependencyClass.class);

        assertThat(instance.say(), is("Hello, World!"));
    }

    @Test
    void returnsBoundClass_WithMultipleDependencies() {
        injector.bind(MultipleDependencyClass.class);
        injector.bind(MultipleDependencyClass.InnerOne.class);
        injector.bind(MultipleDependencyClass.InnerTwo.class);

        var instance = injector.get(MultipleDependencyClass.class);

        assertThat(instance.say(), is("Hello One, World Two!"));
    }

    @Test
    void returnsBoundClass_WithMultiLevelDependencyHierarchy() {
        injector.bind(MultipleLevelDependencyClass.class);
        injector.bind(MultipleLevelDependencyClass.InnerOne.class);
        injector.bind(MultipleLevelDependencyClass.InnerTwo.class);

        var instance = injector.get(MultipleLevelDependencyClass.class);

        assertThat(instance.say(), is("Hello One, World Two!"));
    }

    @Test
    void returnsBoundClass_UsingProvidedInstances() {
        var instanceDependency =
                new SingleDependencyClass.Inner() {
                    @Override
                    public String say() {
                        return "Goodbye, World!";
                    }
                };

        injector.bind(SingleDependencyClass.class);
        injector.bind(SingleDependencyClass.Inner.class, instanceDependency);

        var instance = injector.get(SingleDependencyClass.class);

        assertThat(instance.say(), is("Goodbye, World!"));
    }

    @Test
    void returnsBoundClass_WithSingleValidConstructor() {
        injector.bind(SingleValidConstructorClass.class);

        // Don't bind `InnerOne`, which makes one constructor invalid
        // injector.bind(SingleValidConstructorClass.InnerOne.class);

        injector.bind(SingleValidConstructorClass.InnerTwo.class);

        var instance = injector.get(SingleValidConstructorClass.class);

        assertThat(instance.say(), is("Two"));
    }

    @Test
    void throwsException_ifNoValidConstructors() {
        injector.bind(SingleDependencyClass.class);

        var exception =
                assertThrows(
                        InjectionException.class, () -> injector.get(SingleDependencyClass.class));

        assertThat(
                exception.getMessage(),
                is(
                        "No injectable constructor for [uk.co.probablyfine.inject.SingleDependencyClass]"));
    }

    @Test
    void throwsException_ifMultipleInstancesForSameClass() {
        injector.bind(String.class, "one");

        var exception =
                assertThrows(InjectionException.class, () -> injector.bind(String.class, "two"));

        assertThat(exception.getMessage(), is("Binding already exists for [java.lang.String]"));
    }

    @Test
    void throwsException_ifBindingAttemptedAfterGet() {
        injector.bind(SingleDependencyClass.class);
        injector.bind(SingleDependencyClass.Inner.class);

        injector.get(SingleDependencyClass.class);

        var exception =
                assertThrows(InjectionException.class, () -> injector.bind(SingleDependencyClass.Inner.class));

        assertThat(exception.getMessage(), is("Binding already exists for [uk.co.probablyfine.inject.SingleDependencyClass$Inner]"));
    }

    @Test
    void returnsBoundClass_WithoutReinitialising() {
        injector.bind(EmptyConstructorClass.class);

        var first = injector.get(EmptyConstructorClass.class);
        var second = injector.get(EmptyConstructorClass.class);

        assertThat(first, is(second));
    }
}
