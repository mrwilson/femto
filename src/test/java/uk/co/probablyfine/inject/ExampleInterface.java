package uk.co.probablyfine.inject;

public interface ExampleInterface {

    String say();

    class ImplementsExampleInterface implements ExampleInterface {
        public ImplementsExampleInterface() {}

        @Override
        public String say() {
            return "Hello, World!";
        }
    }

    class DependsOnInterface {
        private final ExampleInterface exampleInterface;

        public DependsOnInterface(ExampleInterface exampleInterface) {
            this.exampleInterface = exampleInterface;
        }

        public String say() {
            return this.exampleInterface.say();
        }
    }
}
