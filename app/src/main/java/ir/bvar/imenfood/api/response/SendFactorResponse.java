package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class SendFactorResponse implements Serializable {

    @SerializedName("image")
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }
}
