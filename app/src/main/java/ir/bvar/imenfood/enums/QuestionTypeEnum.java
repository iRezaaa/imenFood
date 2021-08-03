package ir.bvar.imenfood.enums;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public enum QuestionTypeEnum implements Serializable {
    @SerializedName("advice")
    Advice("advice"),

    @SerializedName("device")
    Device("device");

    private String value;

    QuestionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
