package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 13/1/2018 AD.
 */

public class ErrorResponse implements Serializable {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
