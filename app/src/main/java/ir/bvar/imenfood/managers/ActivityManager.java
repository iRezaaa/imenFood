package ir.bvar.imenfood.managers;

import ir.bvar.imenfood.ui.activities.BaseActivity;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public class ActivityManager {
    private BaseActivity currentActivity;

    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }
}
