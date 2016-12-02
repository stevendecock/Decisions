package be.decock.steven.decisions.model;

public class Answer {

    private String name;
    Rule rule;

    private Answer() {
    }

    public String getName() {
        return name;
    }

    public Rule getRule() {
        return rule;
    }

    public void println() {
        System.out.println(String.format("%s: %s", rule.getName(), name));
    }

    public static class AnswerBuilder {

        private String name;

        public static AnswerBuilder answer(String name) {
            return new AnswerBuilder().withName(name);
        }

        private AnswerBuilder() {}

        public Answer build() {
            Answer rule = new Answer();
            rule.name = name;
            return rule;
        }

        public AnswerBuilder withName(String name) {
            this.name = name;
            return this;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(name, answer.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
