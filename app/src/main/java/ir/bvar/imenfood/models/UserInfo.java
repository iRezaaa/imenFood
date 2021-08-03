package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public class UserInfo implements Serializable {

    @SerializedName("id")
    private
    int ID;

    @SerializedName("email")
    private
    String email;

    @SerializedName("name")
    private
    String name;

    @SerializedName("phone_number")
    private
    long phoneNumber;

    @SerializedName("admin_panel_access")
    private boolean adminPanelAccess = false;

    @SerializedName("authorization_level")
    private
    int authLevel;

    @SerializedName("profile_picture")
    private
    String profilePictureURL;

    @SerializedName("branch")
    private
    BranchInfo branchInfo;

    @SerializedName("Morning")
    private
    boolean morning = false;

    @SerializedName("During")
    private
    boolean during = false;

    @SerializedName("Closing")
    private
    boolean closing = false;


    @SerializedName("Hours")
    CheckupTimes checkupTimes;


    public int getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public int getAuthLevel() {
        return authLevel;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public BranchInfo getBranchInfo() {
        return branchInfo;
    }

    public boolean morningCheckupIsSet() {
        return morning;
    }

    public boolean duringCheckupIsSet() {
        return during;
    }

    public boolean closingCheckupIsSet() {
        return closing;
    }

    public CheckupTimes getCheckupTimes() {
        return checkupTimes;
    }
}
