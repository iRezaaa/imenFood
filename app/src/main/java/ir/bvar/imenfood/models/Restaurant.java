package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public class Restaurant implements Serializable {
    @SerializedName("id")
    private int ID;

    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private String logoURL;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLogoURL() {
        return logoURL;
    }
}
