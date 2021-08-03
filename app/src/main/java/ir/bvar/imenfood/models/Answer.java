package ir.bvar.imenfood.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ir.bvar.imenfood.enums.AnswerTypeEnum;

/**
 * Created by rezapilehvar on 18/1/2018 AD.
 */

public class Answer implements Serializable {

    @SerializedName("answer")
    @Expose
    private boolean answer = true;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("Q_id")
    @Expose
    private int questionID;

    private Answer() {

    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public static class Builder {
        private List<Equipment> equipmentList = new ArrayList<>();
        private int questionID;
        private AnswerTypeEnum type;
        private boolean answer = true;

        public Builder(int questionID) {
            this.questionID = questionID;
        }

        public void addEquipment(Equipment equipment) {
            if (equipment != null) {
                equipmentList.add(equipment);
            }
        }

        public void removeEquipment(Equipment equipment) {
            if (equipment != null) {
                equipmentList.remove(equipment);
            }
        }

        public void setQuestionID(int questionID) {
            this.questionID = questionID;
        }

        public void setType(AnswerTypeEnum type) {
            this.type = type;
        }

        public void setAnswer(boolean answer) {
            this.answer = answer;
        }

        private String getDesc() {
            StringBuilder desc = new StringBuilder();

            for (Equipment equipment : equipmentList) {
                desc.append(equipment.getEquipmentData().getID()).append("-");
            }

            if (desc.toString().endsWith("-")) {
                desc = new StringBuilder(desc.substring(0, desc.length() - 1));
            }

            return desc.toString();
        }

        public Answer build() {
            Answer answer = new Answer();
            answer.setAnswer(Builder.this.answer);
            answer.setDesc(Builder.this.getDesc());
            answer.setType(Builder.this.type.toString());
            answer.setQuestionID(Builder.this.questionID);

            return answer;
        }
    }
}
