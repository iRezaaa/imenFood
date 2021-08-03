package ir.bvar.imenfood.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ir.bvar.imenfood.App;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public class InternetConnectivityChangeBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi.isAvailable() || mobile.isAvailable()) && App.getInstance().isOnline()) {
            if (App.getInstance() != null) {
                App.getInstance().onInternetConnectionChanged(true);
            }
        } else {
            if (App.getInstance() != null) {
                App.getInstance().onInternetConnectionChanged(false);
            }
        }
    }
}
