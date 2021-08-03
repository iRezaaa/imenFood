package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 28/12/2017 AD.
 */

public class LoginResponse implements Serializable {
    @SerializedName("Token")
    private
    String token;

    public String getToken() {
        return token;
    }
}
