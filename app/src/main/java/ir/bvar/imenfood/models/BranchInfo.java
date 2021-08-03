package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public class BranchInfo implements Serializable {

    @SerializedName("id")
    private
    int ID;

    @SerializedName("branch")
    private
    String branch;

    @SerializedName("add")
    private
    String address;

    @SerializedName("email")
    private
    String email;

    @SerializedName("phone_number")
    private
    long phoneNumber;

    @SerializedName("industrial_code")
    private
    int industrialCode;

    @SerializedName("reference_number")
    private
    int refrenceNumber;

    @SerializedName("restaurant")
    private
    Restaurant restaurant;

    public int getID() {
        return ID;
    }

    public String getBranch() {
        return branch;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public int getIndustrialCode() {
        return industrialCode;
    }

    public int getRefrenceNumber() {
        return refrenceNumber;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
