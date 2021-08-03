package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class Provider implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Provider)) return false;

        Provider provider = (Provider) o;

        return getID() == provider.getID();
    }

    @Override
    public int hashCode() {
        return getID();
    }
}
