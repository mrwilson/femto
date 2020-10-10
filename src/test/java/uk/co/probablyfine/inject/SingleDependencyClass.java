package uk.co.probablyfine.inject;

class SingleDependencyClass {
    private final Inner inner;

    static class Inner {
        public Inner() {
        }

        public String say() {
            return "Hello, World!";
        }
    }

    public SingleDependencyClass(Inner inner) {
        this.inner = inner;
    }

    public String say() {
        return this.inner.say();
    }
}
