package be.decock.steven.decisions;

import be.decock.steven.decisions.model.DecisionTable;

import static be.decock.steven.decisions.model.Answer.AnswerBuilder.answer;
import static be.decock.steven.decisions.model.DecisionTable.DecisionTableBuilder.decisionTable;
import static be.decock.steven.decisions.model.Rule.RuleBuilder.rule;

public class Decisions {

    public static void main(String[] args) {
        DecisionTable decisionTable = decisionTable("Resultaat wijziging begindatum")
                .withRules(
                        rule("Type Opleiding")
                                .withAnswers(
                                        answer("Erkend").build(),
                                        answer("Niet Erkend").build()
                                ).build(),
                        rule("Huidige status")
                                .withAnswers(
                                        answer("Definitief Goedgekeurd").build(),
                                        answer("Voorlopig Goedgekeurd").build(),
                                        answer("Geaccepteerd").build()
                                ).build(),
                        rule("Begindatum huidige periode")
                                .withAnswers(
                                        answer("In het verleden of vandaag").build(),
                                        answer("In de toekomst").build()
                                ).build(),
                        rule("Lengte nieuwe periode")
                                .withAnswers(
                                        answer("< 26 dagen").build(),
                                        answer(">= 26 dagen").build()
                                ).build(),
                        rule("Administratief nazicht vandaag")
                                .withAnswers(
                                        answer("OK").build(),
                                        answer("NOK").build()
                                ).build()
                ).build();

        decisionTable.askQuestions();
        String json = decisionTable.toJson();
    }

}
