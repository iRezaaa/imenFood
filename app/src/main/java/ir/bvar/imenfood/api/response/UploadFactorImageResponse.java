package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class UploadFactorImageResponse implements Serializable {
    @SerializedName("image_path")
    private
    String imagePath;

    public String getImagePath() {
        return imagePath;
    }
}
