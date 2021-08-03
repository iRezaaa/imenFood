package ir.bvar.imenfood;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.net.ProtocolException;

import io.reactivex.schedulers.Schedulers;
import ir.bvar.imenfood.broadcasts.InternetConnectivityChangeBroadcast;
import ir.bvar.imenfood.constants.Config;
import ir.bvar.imenfood.managers.ActivityManager;
import ir.bvar.imenfood.managers.SharedPreferencesManager;
import ir.bvar.imenfood.managers.StaticDataManager;
import ir.bvar.imenfood.managers.UpdateManager;
import ir.bvar.imenfood.ui.activities.LoginActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    private ActivityManager activityManager;
    private UpdateManager updateManager;
    private SharedPreferencesManager sharedPreferencesManager;
    private StaticDataManager staticDataManager;

    private OkHttpClient okHttpClientInstance;
    private Retrofit retrofitInstance;
    private Gson gsonInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                getSharedPreferencesManagerInstance().setOneSignalPlayerID(userId);
            }
        });

        // register connection broadcast receiver
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        InternetConnectivityChangeBroadcast connectivityChangeBroadcast = new InternetConnectivityChangeBroadcast();
        registerReceiver(connectivityChangeBroadcast, intentFilter);
    }

    /**
     * Activity Manager Instance
     *
     * @return activityManager {@link ActivityManager}
     */
    public ActivityManager getActivityManager() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }

        return activityManager;
    }

    /**
     * Update Manager Instance
     *
     * @return update manager {@link UpdateManager}
     */
    public UpdateManager getUpdateManager() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }

        return updateManager;
    }

    public Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(Config.API_ADDRESS + "v" + Config.API_VERSION + "/")
                    .client(getOkHttpClientInstance())
                    .addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();
        }

        return retrofitInstance;
    }

    public OkHttpClient getOkHttpClientInstance() {
        if (okHttpClientInstance == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            okHttpClientInstance = new OkHttpClient
                    .Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            if (isOnline()) {
                                Request request = chain.request();
                                Request newRequest;


                                if (getSharedPreferencesManagerInstance().isAuthenticated()) {
                                    newRequest = request.newBuilder()
                                            .header("Authorization", "Token " +
                                                    getSharedPreferencesManagerInstance().getAuthToken())
                                            .build();
                                } else {
                                    newRequest = request.newBuilder().build();
                                }

                                Response response;
                                try {
                                    response = chain.proceed(newRequest);
                                } catch (ProtocolException e) {
                                    response = new Response.Builder()
                                            .request(newRequest)
                                            .code(204)
                                            .protocol(Protocol.HTTP_1_1)
                                            .build();
                                }

                                if (response.code() == 401) {
                                    getSharedPreferencesManagerInstance().setAuthToken(null);
                                    if (getActivityManager().getCurrentActivity() != null && !(getActivityManager().getCurrentActivity() instanceof LoginActivity)) {
                                        getActivityManager().getCurrentActivity().finish();
                                        LoginActivity.start(getActivityManager().getCurrentActivity());
                                    }
                                } else {
                                    return response;
                                }
                            } else {
                                if(App.getInstance().getActivityManager().getCurrentActivity() != null){
                                    App.getInstance().getActivityManager().getCurrentActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(App.getInstance().getActivityManager().getCurrentActivity(), "خطا در برقراری ارتباط با سرور ، از وصل بودن اینترنت مطمعن شوید و دوباره امتحان کنید", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                            return null;
                        }
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();
        }

        return okHttpClientInstance;
    }

    public Gson getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder().serializeNulls().create();
        }

        return gsonInstance;
    }

    public SharedPreferencesManager getSharedPreferencesManagerInstance() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(this);
        }

        return sharedPreferencesManager;
    }

    public StaticDataManager getStaticDataManager() {
        if (staticDataManager == null) {
            staticDataManager = new StaticDataManager();
        }

        return staticDataManager;
    }

    /**
     * On Internet Connection Changed
     * <p>
     * this method called when connection is changed from {@link InternetConnectivityChangeBroadcast}
     */
    public void onInternetConnectionChanged(boolean isConnected) {
        if (isConnected) {
            getUpdateManager().updateDataViews();
        }
    }

    /**
     * Is Online check connectivity
     *
     * @return Boolean
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
