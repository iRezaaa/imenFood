package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ir.bvar.imenfood.enums.QuestionTypeEnum;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public class QuestionFeed implements Serializable {
    @SerializedName("id")
    private int ID;

    @SerializedName("type")
    private
    QuestionTypeEnum questionType;

    @SerializedName("advice")
    private
    String advice;

    @SerializedName("when")
    private
    boolean when;

    @SerializedName("Q_id")
    private
    int questionID;

    public int getID() {
        return ID;
    }

    public QuestionTypeEnum getQuestionType() {
        return questionType;
    }

    public String getAdvice() {
        return advice;
    }

    public boolean when() {
        return when;
    }

    public int getQuestionID() {
        return questionID;
    }
}
