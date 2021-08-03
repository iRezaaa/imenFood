package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class GetLinksResponse implements Serializable {

    @SerializedName("support")
    private
    String supportLink;

    @SerializedName("rules")
    private
    String rulesLink;

    public String getSupportLink() {
        return supportLink;
    }

    public String getRulesLink() {
        return rulesLink;
    }
}
