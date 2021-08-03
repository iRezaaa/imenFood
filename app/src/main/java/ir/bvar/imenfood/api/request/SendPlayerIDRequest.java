package ir.bvar.imenfood.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class SendPlayerIDRequest implements Serializable {
    @SerializedName("player_id")
    private
    String playerID;

    public SendPlayerIDRequest(String playerID) {
        this.playerID = playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
}
