package be.decock.steven.decisions.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static be.decock.steven.decisions.model.Outcome.OutcomeBuilder.outcome;

public class DecisionTable {

    public static final String FILENAME = System.getProperty("user.home") + "\\decisionTable.json";
    private List<Rule> rules = new ArrayList<>();
    private String name;
    private Map<Answer, SubTableOrOutcome> mainTable = new HashMap<>();

    private Stack<Answer> workingListOfanswers = new Stack<>();
    private Scanner scanner = new Scanner(System.in);

    private DecisionTable() {
    }

    public String getName() {
        return name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public String toJson() {
        return "json";
    }

    public void askQuestions() {

        Map<Answer, SubTableOrOutcome> subTable = mainTable;

        walkSubTable(subTable, 0);

    }

    private void walkSubTable(Map<Answer, SubTableOrOutcome> subTable, int level) {
        boolean onFinalRuleLevel = level == rules.size()-1;
        getAnswersForRuleNumber(level)
                .forEach(answer -> {
                    if (!subTable.containsKey(answer)) {
                        System.out.println("Please consider the following situation:");
                        System.out.println("----------------------------------------");
                        printCurrentWalkState();
                        answer.println();
                        boolean outcomeClear = onFinalRuleLevel || askYesOrNo("Is the outcome clear in this situation?");
                        if (outcomeClear) {
                            String outcome = askForInput("What is the outcome?");
                            subTable.put(answer, new SubTableOrOutcome(outcome(outcome).build()));
                            save();
                        } else {
                            subTable.put(answer, new SubTableOrOutcome(new HashMap<>()));
                        }
                    }
                    if (!subTable.get(answer).hasOutCome()) {
                        workingListOfanswers.push(answer);
                        walkSubTable(subTable.get(answer).getSubTable().get(), level + 1);
                    }
                });
        workingListOfanswers.pop();
    }

    private void save() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        try {
            mapper.writeValue(new File(FILENAME), mainTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        ObjectMapper mapper = new ObjectMapper();

        //JSON from file to Object
        try {
            mainTable = mapper.readValue(new File(FILENAME), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String askForInput(String question) {
        System.out.println(question);
        return scanner.next();
    }

    private boolean askYesOrNo(String question) {
        String response = null;
        do {
            System.out.println(String.format("%s (Y/N)", question));
            response = scanner.next().toLowerCase();
        } while (response == null || !(response.equals("y") || response.equals("n")));
        switch (response) {
            case "y": return true;
            case "n": return false;
            default: throw new RuntimeException("Response neither 'y' or 'n'.");
        }
    }

    private void printCurrentWalkState() {
        workingListOfanswers.forEach(Answer::println);
    }

    private Stream<Answer> getAnswersForRuleNumber(int ruleLevel) {
        return rules.get(ruleLevel).getAnswers();
    }


    private static class SubTableOrOutcome {

        private Optional<Outcome> outcome = Optional.empty();
        private Optional<Map<Answer, SubTableOrOutcome>> subTable = Optional.empty();

        private SubTableOrOutcome(Outcome outcome) {
            this.outcome = Optional.of(outcome);
        }

        private SubTableOrOutcome(Map<Answer, SubTableOrOutcome> subTable) {
            this.subTable = Optional.of(subTable);
        }

        public boolean hasOutCome() {
            return outcome.isPresent();
        }

        public Optional<Outcome> getOutcome() {
            return outcome;
        }

        public Optional<Map<Answer, SubTableOrOutcome>> getSubTable() {
            return subTable;
        }
    }

    public static class DecisionTableBuilder {

        private List<Rule> rules;
        private String name;

        public static DecisionTableBuilder decisionTable(String name) {
            return  new DecisionTableBuilder().withName(name);
        }

        private DecisionTableBuilder withName(String name) {
            this.name = name;
            return this;
        }

        private DecisionTableBuilder() {}

        public DecisionTable build() {
            DecisionTable decisionTable = new DecisionTable();
            decisionTable.name = this.name;
            decisionTable.rules = this.rules;
            return decisionTable;
        }

        public DecisionTableBuilder withRules(Rule... rules) {
            this.rules = Arrays.asList(rules);
            return this;
        }

    }

}
