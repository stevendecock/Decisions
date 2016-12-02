package be.decock.steven.decisions.model;

public class Outcome {

    private final String name;

    private Outcome(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class OutcomeBuilder {

        private String name;

        private OutcomeBuilder() {}

        public static OutcomeBuilder outcome(String name) {
            return new OutcomeBuilder().withName(name);
        }

        public Outcome build() {
            return new Outcome(name);
        }

        public OutcomeBuilder withName(String name) {
            this.name = name;
            return this;
        }

    }

}
