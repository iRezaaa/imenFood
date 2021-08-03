package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class SignupResponse implements Serializable {
    @SerializedName("id")
    private int ID;

    @SerializedName("manager_name")
    private String managerName;

    @SerializedName("restaurant_name")
    private String restaurantName;

    @SerializedName("add")
    private String address;

    @SerializedName("email")
    private String email;

    @SerializedName("phone_number")
    private long phoneNumber;

    public int getID() {
        return ID;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getRestaurantName() {
        return restaurantName;
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

    public class Error implements Serializable {
        @Expose
        @SerializedName("email")
        private
        List<String> emailError;

        @Expose
        @SerializedName("restaurant_name")
        private
        List<String> restaurnalError;

        public List<String> getEmailErrors() {
            return emailError;
        }

        public List<String> getRestaurnalError() {
            return restaurnalError;
        }
    }
}
