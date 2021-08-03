package ir.bvar.imenfood.utils;

import java.util.ArrayList;
import java.util.List;

import ir.bvar.imenfood.enums.AnswerTypeEnum;
import ir.bvar.imenfood.enums.QuestionTypeEnum;
import ir.bvar.imenfood.models.Answer;
import ir.bvar.imenfood.models.Equipment;
import ir.bvar.imenfood.models.Question;

/**
 * Created by rezapilehvar on 18/1/2018 AD.
 */

public class CollectionsUtility {
    public static List<Answer> convertQuestionsToAnswer(List<Question> questionList) {
        List<Answer> answerList = new ArrayList<>();

        for (Question question : questionList) {
            if (question != null) {
                Answer.Builder answerBuilder = new Answer.Builder(question.getID());
                if (question.getQuestionType() == QuestionTypeEnum.Device) {
                    for (Equipment equipment : question.getDeviceList()) {
                        if (equipment.isChecked())
                            answerBuilder.addEquipment(equipment);
                    }

                    answerBuilder.setType(AnswerTypeEnum.List);
                } else {
                    answerBuilder.setType(AnswerTypeEnum.Advice);
                }

                answerBuilder.setAnswer(question.getAnswer());
                answerList.add(answerBuilder.build());
            }
        }

        return answerList;
    }
}
