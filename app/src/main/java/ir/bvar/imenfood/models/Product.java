package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class Product implements Serializable {

    @SerializedName("id")
    private int ID;

    @SerializedName("name")
    private String name;

    private boolean isChecked = false;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
