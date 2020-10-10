package uk.co.probablyfine.inject;

class SingleValidConstructorClass {
    private final InnerOne innerOne;
    private final InnerTwo innerTwo;

    static class InnerOne {
        public InnerOne() {}

        public String say() {
            return "One";
        }
    }

    static class InnerTwo {
        public InnerTwo() {}

        public String say() {
            return "Two";
        }
    }

    public SingleValidConstructorClass(InnerOne innerOne) {
        this.innerOne = innerOne;
        this.innerTwo = null;
    }

    public SingleValidConstructorClass(InnerTwo innerTwo) {
        this.innerOne = null;
        this.innerTwo = innerTwo;
    }

    public String say() {
        return this.innerOne == null ? this.innerTwo.say() : this.innerOne.say();
    }
}
