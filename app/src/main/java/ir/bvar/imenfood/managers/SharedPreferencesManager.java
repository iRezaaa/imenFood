package ir.bvar.imenfood.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import ir.bvar.imenfood.App;
import ir.bvar.imenfood.constants.SharedPreferencesKeys;
import ir.bvar.imenfood.enums.CheckUpTypeEnum;
import ir.bvar.imenfood.models.Question;
import ir.bvar.imenfood.models.SharedPreferencesCheckup;

/**
 * Created by rezapilehvar on 16/1/2018 AD.
 */

public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesKeys.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public String getAuthToken() {
        return sharedPreferences.getString(SharedPreferencesKeys.SHARED_PREFERENCES_AUTH_KEY, null);
    }

    public void setAuthToken(String authKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_AUTH_KEY, authKey);
        editor.apply();
    }

    public String getOneSignalPlayerID() {
        return sharedPreferences.getString(SharedPreferencesKeys.SHARED_PREFERENCES_ONESIGNAL_PLAYERID_KEY, null);
    }

    public void setOneSignalPlayerID(String playerID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_ONESIGNAL_PLAYERID_KEY, playerID);
        editor.apply();
    }

    public void saveCheckUp(List<Question> questionList, CheckUpTypeEnum checkUpType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (questionList == null) {
            switch (checkUpType) {
                case Morning:
                    editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_MORNING_KEY, null);
                    break;
                case During:
                    editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_DURING_KEY, null);
                    break;
                case Closing:
                    editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_CLOSING_KEY, null);
                    break;
            }
        } else if (questionList.size() > 0) {
            SharedPreferencesCheckup sharedPreferencesCheckup = new SharedPreferencesCheckup(System.currentTimeMillis(), questionList);

            try {

                switch (checkUpType) {
                    case Morning:
                        editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_MORNING_KEY, App.getInstance().getGsonInstance().toJson(sharedPreferencesCheckup));
                        break;
                    case During:
                        editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_DURING_KEY, App.getInstance().getGsonInstance().toJson(sharedPreferencesCheckup));
                        break;
                    case Closing:
                        editor.putString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_CLOSING_KEY, App.getInstance().getGsonInstance().toJson(sharedPreferencesCheckup));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SharedPreferencesCheckup getSavedCheckup(CheckUpTypeEnum checkUpType) {
        String sharedPreferencesCheckupString = null;

        switch (checkUpType) {
            case Morning:
                sharedPreferencesCheckupString = sharedPreferences.getString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_MORNING_KEY, null);
                break;
            case During:
                sharedPreferencesCheckupString = sharedPreferences.getString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_DURING_KEY, null);
                break;
            case Closing:
                sharedPreferencesCheckupString = sharedPreferences.getString(SharedPreferencesKeys.SHARED_PREFERENCES_CHECKUP_CLOSING_KEY, null);
                break;
        }

        try {
            if (sharedPreferencesCheckupString != null) {
                return App.getInstance().getGsonInstance().fromJson(sharedPreferencesCheckupString, SharedPreferencesCheckup.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isAuthenticated() {
        return sharedPreferences.getString(SharedPreferencesKeys.SHARED_PREFERENCES_AUTH_KEY, null) != null;
    }
}
