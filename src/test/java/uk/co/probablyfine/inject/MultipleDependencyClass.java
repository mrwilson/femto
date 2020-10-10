package uk.co.probablyfine.inject;

class MultipleDependencyClass {
    private final InnerOne innerOne;
    private final InnerTwo innerTwo;

    static class InnerOne {
        public InnerOne() {}

        public String say() {
            return "Hello One";
        }
    }

    static class InnerTwo {
        public InnerTwo() {}

        public String say() {
            return "World Two!";
        }
    }

    public MultipleDependencyClass(InnerOne innerOne, InnerTwo innerTwo) {
        this.innerOne = innerOne;
        this.innerTwo = innerTwo;
    }

    public String say() {
        return this.innerOne.say() + ", " + this.innerTwo.say();
    }
}
