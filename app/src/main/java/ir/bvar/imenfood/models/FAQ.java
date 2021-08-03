package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class FAQ implements Serializable {


    @SerializedName("id")
    private int ID;

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;


    private boolean expanded = false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
