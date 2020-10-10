package uk.co.probablyfine.inject;

class MultipleLevelDependencyClass {
    private final InnerTwo innerTwo;

    static class InnerOne {
        public InnerOne() {
        }

        public String say() {
            return "Hello One";
        }
    }

    static class InnerTwo {
        private final InnerOne innerOne;

        public InnerTwo(InnerOne innerOne) {
            this.innerOne = innerOne;
        }

        public String say() {
            return this.innerOne.say() + ", World Two!";
        }
    }

    public MultipleLevelDependencyClass(InnerTwo innerTwo) {
        this.innerTwo = innerTwo;
    }

    public String say() {
        return this.innerTwo.say();
    }
}
