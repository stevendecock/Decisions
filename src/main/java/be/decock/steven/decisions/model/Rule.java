package be.decock.steven.decisions.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Rule {


    private final String name;
    private final List<Answer> answers;

    private Rule(String name, List<Answer> answers) {
        this.name = name;
        this.answers = answers;
        this.answers.forEach(answer -> answer.rule = this);
    }

    public String getName() {
        return name;
    }

    public Stream<Answer> getAnswers() {
        return answers.stream();
    }

    public static class RuleBuilder {

        private String name;
        private List<Answer> answers;

        public static RuleBuilder rule(String name) {
            return new RuleBuilder().withName(name);
        }

        private RuleBuilder() {}

        public Rule build() {
            return new Rule(name, answers);
        }

        public RuleBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RuleBuilder withAnswers(Answer... answers) {
            this.answers = Arrays.asList(answers);
            return this;
        }
    }

}
