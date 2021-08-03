package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ir.bvar.imenfood.enums.CheckUpTypeEnum;
import ir.bvar.imenfood.enums.QuestionTypeEnum;

/**
 * Created by rezapilehvar on 24/12/2017 AD.
 */

public class Question implements Serializable {

    @SerializedName("id")
    private int ID;

    @SerializedName("text")
    private String text;

    @SerializedName("section")
    private CheckUpTypeEnum checkUpTypeEnum;

    private boolean answer = true;


    /**
     * QuestionFeed Data
     */
    private QuestionTypeEnum questionType;
    private String advice;
    private boolean when;

    private boolean done = false;
    private boolean falseAnswerClicked = false;


    private List<Equipment> deviceList;


    public void setDeviceList(List<Equipment> deviceList) {
        this.deviceList = deviceList;
    }

    public int getID() {
        return ID;
    }

    public String getText() {
        return text;
    }

    public CheckUpTypeEnum getCheckUpTypeEnum() {
        return checkUpTypeEnum;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isFalseAnswerClicked() {
        return falseAnswerClicked;
    }

    public void setFalseAnswerClicked(boolean falseAnswerClicked) {
        this.falseAnswerClicked = falseAnswerClicked;
    }

    public QuestionTypeEnum getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionTypeEnum questionType) {
        this.questionType = questionType;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public boolean when() {
        return when;
    }

    public void setWhen(boolean when) {
        this.when = when;
    }

    public List<Equipment> getDeviceList() {
        return deviceList;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
