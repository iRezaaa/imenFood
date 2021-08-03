package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rezapilehvar on 13/2/2018 AD.
 */

public class SharedPreferencesCheckup implements Serializable {

    @SerializedName("time")
    private
    long timestamp;

    @SerializedName("checkup_list")
    private
    List<Question> checkUpList;

    public SharedPreferencesCheckup(long timestamp, List<Question> checkUpList) {
        this.timestamp = timestamp;
        this.checkUpList = checkUpList;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Question> getCheckUpList() {
        return checkUpList;
    }
}
