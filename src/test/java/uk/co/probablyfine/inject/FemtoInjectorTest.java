package uk.co.probablyfine.inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import uk.co.probablyfine.inject.ExampleInterface.DependsOnInterface;
import uk.co.probablyfine.inject.ExampleInterface.ImplementsExampleInterface;

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
        injector.bindInstance(SingleDependencyClass.Inner.class, instanceDependency);

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
        injector.bindInstance(String.class, "one");

        var exception =
                assertThrows(
                        InjectionException.class, () -> injector.bindInstance(String.class, "two"));

        assertThat(exception.getMessage(), is("Binding already exists for [java.lang.String]"));
    }

    @Test
    void throwsException_ifBindingAttemptedAfterGet() {
        injector.bind(SingleDependencyClass.class);
        injector.bind(SingleDependencyClass.Inner.class);

        injector.get(SingleDependencyClass.class);

        var exception =
                assertThrows(
                        InjectionException.class,
                        () -> injector.bind(SingleDependencyClass.Inner.class));

        assertThat(
                exception.getMessage(),
                is(
                        "Binding already exists for [uk.co.probablyfine.inject.SingleDependencyClass$Inner]"));
    }

    @Test
    void returnsBoundClass_WithoutReinitialising() {
        injector.bind(EmptyConstructorClass.class);

        var first = injector.get(EmptyConstructorClass.class);
        var second = injector.get(EmptyConstructorClass.class);

        assertThat(first, is(second));
    }

    @Test
    void returnsBoundClass_BindingToInterface() {
        injector.bindInstance(ExampleInterface.class, new ImplementsExampleInterface());
        injector.bind(DependsOnInterface.class);

        var instance = injector.get(DependsOnInterface.class);

        assertThat(instance.say(), is("Hello, World!"));
    }

    @Test
    void returnsBoundClass_BindingToInterfaceClass() {
        injector.bind(ExampleInterface.class, ImplementsExampleInterface.class);
        injector.bind(DependsOnInterface.class);

        var instance = injector.get(DependsOnInterface.class);

        assertThat(instance.say(), is("Hello, World!"));
    }

    @Test
    void throwsException_nullBindingClassPassed() {
        var exception = assertThrows(IllegalArgumentException.class, () -> injector.bind(null));

        assertThat(exception.getMessage(), is("Binding class must not be null"));
    }

    @Test
    void throwsException_nullBindingClassPassedAsInstanceBinding() {
        var instance = new Object();

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> injector.bindInstance(null, instance));

        assertThat(exception.getMessage(), is("Binding class must not be null"));
    }

    @Test
    void throwsException_nullBindingInstancePassedAsInstanceBinding() {
        var instance = new Object();

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> injector.bindInstance(SingleDependencyClass.class, null));

        assertThat(exception.getMessage(), is("Binding instance must not be null"));
    }

    @Test
    void throwsException_nullBindingClassPassedAsSuperclassBinding() {
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> injector.bind(null, SingleDependencyClass.class));

        assertThat(exception.getMessage(), is("Binding superclass must not be null"));
    }

    @Test
    void throwsException_nullBindingImplementationPassedAsSuperclassBinding() {
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> injector.bind(SingleDependencyClass.class, null));

        assertThat(exception.getMessage(), is("Binding implementation class must not be null"));
    }
}
