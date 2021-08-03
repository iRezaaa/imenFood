package ir.bvar.imenfood.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public enum AnswerTypeEnum {
    @SerializedName("device")
    List("device"),

    @SerializedName("advice")
    Advice("advice");

    private String value;


    AnswerTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
