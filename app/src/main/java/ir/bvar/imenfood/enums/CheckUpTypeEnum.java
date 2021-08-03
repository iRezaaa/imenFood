package ir.bvar.imenfood.enums;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public enum CheckUpTypeEnum implements Serializable {

    @SerializedName("morning")
    Morning("morning"),
    @SerializedName("during")
    During("during"),
    @SerializedName("closing")
    Closing("closing");

    private String value;

    CheckUpTypeEnum(String value) {
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
