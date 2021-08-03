package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class CheckupTimes implements Serializable {
    @SerializedName("morning")
    private
    CheckupTimeData morningTime;

    @SerializedName("during")
    private
    CheckupTimeData duringTime;

    @SerializedName("closing")
    private
    CheckupTimeData closingTime;

    public CheckupTimeData getMorningTime() {
        return morningTime;
    }

    public CheckupTimeData getDuringTime() {
        return duringTime;
    }

    public CheckupTimeData getClosingTime() {
        return closingTime;
    }

    public class CheckupTimeData implements Serializable {
        @SerializedName("start")
        private int startTime;

        @SerializedName("stop")
        private int stopTime;

        public int getStartTime() {
            return startTime;
        }

        public int getStopTime() {
            return stopTime;
        }
    }
}
