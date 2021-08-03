package ir.bvar.imenfood.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class SignupRequest implements Serializable {
    @SerializedName("manager_name")
    private
    String fullName;

    @SerializedName("restaurant_name")
    private
    String restaurantName;

    @SerializedName("add")
    private
    String address;

    @SerializedName("email")
    private
    String email;

    @SerializedName("phone_number")
    private
    String phoneNumber;


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
