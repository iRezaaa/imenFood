package ir.bvar.imenfood.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class GetProductRequest implements Serializable {

    @SerializedName("id")
    private int providerID;

    public GetProductRequest(int providerID) {
        this.providerID = providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }
}
