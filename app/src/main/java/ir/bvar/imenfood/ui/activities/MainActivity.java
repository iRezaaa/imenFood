package ir.bvar.imenfood.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.bvar.imenfood.App;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.api.AuthApi;
import ir.bvar.imenfood.api.EquipmentApi;
import ir.bvar.imenfood.api.LinksApi;
import ir.bvar.imenfood.api.NotificationApi;
import ir.bvar.imenfood.api.UserApi;
import ir.bvar.imenfood.api.request.SendPlayerIDRequest;
import ir.bvar.imenfood.api.response.GetLinksResponse;
import ir.bvar.imenfood.api.response.LogOutResponse;
import ir.bvar.imenfood.api.response.SendPlayerIDResponse;
import ir.bvar.imenfood.constants.Config;
import ir.bvar.imenfood.enums.CheckUpTypeEnum;
import ir.bvar.imenfood.models.CheckupTimes;
import ir.bvar.imenfood.models.Equipment;
import ir.bvar.imenfood.models.UserInfo;

public class MainActivity extends BaseActivity {
    private static final String TIME_PASSED = "TIME_PASSED";
    private static final String TIME_AGO = "TIME_AGO";
    private static final String IS_DONE = "IS_DONE";

    @BindView(R.id.activity_main_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.activity_main_drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar_activity_main_drawerButton)
    AppCompatImageView drawerButton;

    @BindView(R.id.toolbar_activity_main_messagesButton)
    AppCompatImageView messagesButton;

    @BindView(R.id.navigation_activity_main_userFullnameTextView)
    AppCompatTextView userFullnameTextView;

    @BindView(R.id.navigation_activity_main_userImageView)
    AppCompatImageView navigationUserImageView;

    @BindView(R.id.activity_main_checkSectionMorning)
    RelativeLayout checkSectionMorning;
    @BindView(R.id.activity_main_checkSectionMorningDoneImageView)
    AppCompatImageView checkSectionMorningDoneImageView;
    @BindView(R.id.activity_main_checkSelectionMorningBackground)
    View checkSectionMorningBackground;
    @BindView(R.id.activity_main_checkSectionMorningToastMakerView)
    View checkSectionMorningToastMakerView;

    @BindView(R.id.activity_main_checkSectionDuring)
    RelativeLayout checkSectionDuring;
    @BindView(R.id.activity_main_checkSectionDuringDoneImageView)
    AppCompatImageView checkSectionDuringDoneImageView;
    @BindView(R.id.activity_main_checkSelectionDuringBackground)
    View checkSectionDuringBackground;
    @BindView(R.id.activity_main_checkSectionDuringToastMakerView)
    View checkSectionDuringToastMakerView;

    @BindView(R.id.activity_main_checkSectionClosing)
    RelativeLayout checkSectionClosing;
    @BindView(R.id.activity_main_checkSectionClosingDoneImageView)
    AppCompatImageView checkSectionClosingDoneImageView;
    @BindView(R.id.activity_main_checkSelectionClosingBackground)
    View checkSectionClosingBackground;
    @BindView(R.id.activity_main_checkSectionClosingToastMakerView)
    View checkSectionClosingToastMakerView;

    private Disposable getEquipmentDisposable;
    private Disposable getUserInfoDisposable;
    private Disposable userLogoutDisposable;
    private Disposable getLinksDisposable;
    private Disposable sendOneSignalPlayerIDToServerDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkAuth();
    }

    private void checkAuth() {
        if (App.getInstance().getSharedPreferencesManagerInstance().isAuthenticated()) {
            initViews();
            updateEquipmentList();
            getUserInfo();
            getLinks();
        } else {
            LoginActivity.start(this);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.getInstance().getSharedPreferencesManagerInstance().isAuthenticated()) {
            getUserInfo();
            sendOneSignalPlayerID();
        }
    }

    private void initViews() {
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageListActivity.start(MainActivity.this);
            }
        });
    }

    public void onActionClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_activity_main_sendFactorAction:
                SendFactorActivity.start(this);
                break;
            case R.id.navigation_activity_main_helpAction:
                HelpActivity.start(this);
                break;
            case R.id.navigation_activity_main_supportAction:
                if (App.getInstance().getStaticDataManager().getSupportLink() != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(App.getInstance().getStaticDataManager().getSupportLink()));
                    startActivity(i);
                }
                break;
            case R.id.navigation_activity_main_logOutAction:
                logOutUser();
                break;
        }
    }

    public void onToastMakerClick(View view) {
        if (view.getTag() != null && view.getVisibility() == View.VISIBLE) {
            if (view.getTag() == TIME_PASSED) {
                Toast.makeText(this, "زمان پاسخ دهی گذشته", Toast.LENGTH_SHORT).show();
            } else if (view.getTag() == TIME_AGO) {
                Toast.makeText(this, "زمان پاسخ دهی هنوز نرسیده است", Toast.LENGTH_SHORT).show();
            }else if(view.getTag() == IS_DONE){
                Toast.makeText(this, "سوالات پاسخ داده شده است", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onCheckSectionsClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_checkSectionMorning:
                CheckUpActivity.start(this, CheckUpTypeEnum.Morning);
                break;
            case R.id.activity_main_checkSectionDuring:
                CheckUpActivity.start(this, CheckUpTypeEnum.During);
                break;
            case R.id.activity_main_checkSectionClosing:
                CheckUpActivity.start(this, CheckUpTypeEnum.Closing);
                break;
        }
    }

    private void logOutUser() {
        if (userLogoutDisposable != null && !userLogoutDisposable.isDisposed()) {
            userLogoutDisposable.dispose();
        }

        userLogoutDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(AuthApi.class)
                .logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LogOutResponse>() {

                    @Override
                    public void onNext(LogOutResponse logOutResponse) {
                        App.getInstance().getSharedPreferencesManagerInstance().setAuthToken(null);
                        LoginActivity.start(MainActivity.this);
                        finish();
                        Toast.makeText(MainActivity.this, "با موفقیت از حساب کاربری خارج شدید", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        App.getInstance().getSharedPreferencesManagerInstance().setAuthToken(null);
                        LoginActivity.start(MainActivity.this);
                        finish();
                        Toast.makeText(MainActivity.this, "با موفقیت از حساب کاربری خارج شدید", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void sendOneSignalPlayerID() {
        if (sendOneSignalPlayerIDToServerDisposable != null && !sendOneSignalPlayerIDToServerDisposable.isDisposed()) {
            sendOneSignalPlayerIDToServerDisposable.dispose();
        }

        if (App.getInstance().getSharedPreferencesManagerInstance().getOneSignalPlayerID() != null) {
            sendOneSignalPlayerIDToServerDisposable = App.getInstance()
                    .getRetrofitInstance()
                    .create(NotificationApi.class)
                    .sendOneSignalPlayerID(new SendPlayerIDRequest(App.getInstance().getSharedPreferencesManagerInstance().getOneSignalPlayerID()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<SendPlayerIDResponse>() {
                        @Override
                        public void onNext(SendPlayerIDResponse sendPlayerIDResponse) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void getLinks() {
        if (getLinksDisposable != null && !getLinksDisposable.isDisposed()) {
            getLinksDisposable.dispose();
        }

        getLinksDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(LinksApi.class)
                .getLinks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<GetLinksResponse>() {
                    @Override
                    public void onNext(GetLinksResponse getLinksResponse) {
                        if (getLinksResponse != null) {
                            App.getInstance().getStaticDataManager().setSupportLink(getLinksResponse.getSupportLink());
                            App.getInstance().getStaticDataManager().setRulesLink(getLinksResponse.getRulesLink());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateEquipmentList() {
        getEquipmentDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(EquipmentApi.class)
                .getEquipmentList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Equipment>>() {

                    @Override
                    public void onNext(List<Equipment> equipments) {
                        if (equipments != null) {
                            App.getInstance().getStaticDataManager().setEquipmentList(equipments);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateEquipmentList();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getUserInfo() {
        if (getUserInfoDisposable != null && !getUserInfoDisposable.isDisposed()) {
            getUserInfoDisposable.dispose();
        }

        getUserInfoDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(UserApi.class)
                .getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserInfo>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadingLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        App.getInstance().getStaticDataManager().setCheckUpTimes(userInfo.getCheckupTimes());
                        App.getInstance().getStaticDataManager().setCurrentUserInfo(userInfo);
                        userFullnameTextView.setText(userInfo.getName());

                        if (userInfo.getProfilePictureURL() != null && !userInfo.getProfilePictureURL().isEmpty()) {
                            Glide.with(App.getInstance()).load(Config.IMAGES_ADDRESS + userInfo.getProfilePictureURL()).into(navigationUserImageView);
                        } else {
                            navigationUserImageView.setImageResource(R.drawable.placeholder_user);
                        }

                        CheckupTimes checkupTimes = App.getInstance().getStaticDataManager().getCheckupTimes();

                        int currentHourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

                        if (checkupTimes != null) {
                            if (!userInfo.morningCheckupIsSet()) {

                                checkSectionMorningBackground.setAlpha(0.3f);
                                checkSectionMorningToastMakerView.setVisibility(View.GONE);

                                if (checkupTimes.getMorningTime().getStartTime() <= currentHourOfDay
                                        && checkupTimes.getMorningTime().getStopTime() >= currentHourOfDay) {
                                    checkSectionMorning.setEnabled(true);
                                    checkSectionMorning.setClickable(true);
                                    checkSectionMorning.setFocusable(true);
                                    checkSectionMorning.setAlpha(1f);
                                    checkSectionMorningDoneImageView.setVisibility(View.GONE);
                                    checkSectionMorningBackground.setBackgroundResource(android.R.color.black);
                                    checkSectionMorningToastMakerView.setVisibility(View.GONE);
                                } else {
                                    checkSectionMorning.setEnabled(false);
                                    checkSectionMorning.setClickable(false);
                                    checkSectionMorning.setFocusable(false);
                                    checkSectionMorning.setAlpha(1f);
                                    checkSectionMorningDoneImageView.setImageResource(R.drawable.ic_timer_off_black_36dp);
                                    checkSectionMorningDoneImageView.setVisibility(View.VISIBLE);

                                    if (currentHourOfDay >= checkupTimes.getMorningTime().getStopTime()) {
                                        checkSectionMorningBackground.setBackgroundResource(android.R.color.holo_red_light);
                                        checkSectionMorningToastMakerView.setVisibility(View.VISIBLE);
                                        checkSectionMorningToastMakerView.setTag(TIME_PASSED);
                                    } else if (currentHourOfDay <= checkupTimes.getMorningTime().getStartTime()) {
                                        checkSectionMorningBackground.setAlpha(0.8f);
                                        checkSectionMorningBackground.setBackgroundResource(R.color.colorGray);
                                        checkSectionMorningToastMakerView.setVisibility(View.VISIBLE);
                                        checkSectionMorningToastMakerView.setTag(TIME_AGO);
                                    }
                                }
                            } else {
                                checkSectionMorningBackground.setAlpha(0.3f);
                                checkSectionMorningToastMakerView.setVisibility(View.VISIBLE);
                                checkSectionMorningToastMakerView.setTag(IS_DONE);

                                checkSectionMorning.setEnabled(false);
                                checkSectionMorning.setClickable(false);
                                checkSectionMorning.setFocusable(false);
                                checkSectionMorning.setAlpha(1f);
                                checkSectionMorningDoneImageView.setImageResource(R.drawable.ic_done_all_black_36dp);
                                checkSectionMorningDoneImageView.setVisibility(View.VISIBLE);
                                checkSectionMorningBackground.setBackgroundResource(android.R.color.holo_green_light);
                            }

                            if (!userInfo.duringCheckupIsSet()) {

                                checkSectionDuringBackground.setAlpha(0.3f);
                                checkSectionDuringToastMakerView.setVisibility(View.GONE);

                                if (checkupTimes.getDuringTime().getStartTime() <= currentHourOfDay
                                        && checkupTimes.getDuringTime().getStopTime() >= currentHourOfDay) {
                                    checkSectionDuring.setEnabled(true);
                                    checkSectionDuring.setClickable(true);
                                    checkSectionDuring.setFocusable(true);
                                    checkSectionDuring.setAlpha(1f);
                                    checkSectionDuringDoneImageView.setVisibility(View.GONE);
                                    checkSectionDuringBackground.setBackgroundResource(android.R.color.black);
                                } else {
                                    checkSectionDuring.setEnabled(false);
                                    checkSectionDuring.setClickable(false);
                                    checkSectionDuring.setFocusable(false);
                                    checkSectionDuring.setAlpha(1f);
                                    checkSectionDuringDoneImageView.setImageResource(R.drawable.ic_timer_off_black_36dp);
                                    checkSectionDuringDoneImageView.setVisibility(View.VISIBLE);

                                    if (currentHourOfDay >= checkupTimes.getDuringTime().getStopTime()) {
                                        checkSectionDuringBackground.setBackgroundResource(android.R.color.holo_red_light);
                                        checkSectionDuringToastMakerView.setVisibility(View.VISIBLE);
                                        checkSectionDuringToastMakerView.setTag(TIME_PASSED);
                                    } else if (currentHourOfDay <= checkupTimes.getDuringTime().getStartTime()) {
                                        checkSectionDuringBackground.setAlpha(0.8f);
                                        checkSectionDuringBackground.setBackgroundResource(R.color.colorGray);
                                        checkSectionDuringToastMakerView.setVisibility(View.VISIBLE);
                                        checkSectionDuringToastMakerView.setTag(TIME_AGO);
                                    }
                                }
                            } else {
                                checkSectionDuringBackground.setAlpha(0.3f);
                                checkSectionDuringToastMakerView.setVisibility(View.VISIBLE);
                                checkSectionDuringToastMakerView.setTag(IS_DONE);

                                checkSectionDuring.setEnabled(false);
                                checkSectionDuring.setClickable(false);
                                checkSectionDuring.setFocusable(false);
                                checkSectionDuring.setAlpha(1f);
                                checkSectionDuringDoneImageView.setImageResource(R.drawable.ic_done_all_black_36dp);
                                checkSectionDuringDoneImageView.setVisibility(View.VISIBLE);
                                checkSectionDuringBackground.setBackgroundResource(android.R.color.holo_green_light);
                            }

                            if (!userInfo.closingCheckupIsSet()) {

                                checkSectionClosingBackground.setAlpha(0.3f);
                                checkSectionClosingToastMakerView.setVisibility(View.GONE);

                                if (checkupTimes.getClosingTime().getStartTime() <= currentHourOfDay
                                        && checkupTimes.getClosingTime().getStopTime() >= currentHourOfDay) {
                                    checkSectionClosing.setEnabled(true);
                                    checkSectionClosing.setClickable(true);
                                    checkSectionClosing.setFocusable(true);
                                    checkSectionClosing.setAlpha(1f);
                                    checkSectionClosingDoneImageView.setVisibility(View.GONE);
                                    checkSectionClosingBackground.setBackgroundResource(android.R.color.black);
                                } else {
                                    checkSectionClosing.setEnabled(false);
                                    checkSectionClosing.setClickable(false);
                                    checkSectionClosing.setFocusable(false);
                                    checkSectionClosing.setAlpha(1f);
                                    checkSectionClosingDoneImageView.setImageResource(R.drawable.ic_timer_off_black_36dp);
                                    checkSectionClosingDoneImageView.setVisibility(View.VISIBLE);

                                    if (currentHourOfDay >= checkupTimes.getClosingTime().getStopTime()) {
                                        checkSectionClosingBackground.setBackgroundResource(android.R.color.holo_red_light);
                                        checkSectionClosingToastMakerView.setVisibility(View.VISIBLE);
                                        checkSectionClosingToastMakerView.setTag(TIME_PASSED);
                                    } else if (currentHourOfDay <= checkupTimes.getClosingTime().getStartTime()) {
                                        checkSectionClosingBackground.setAlpha(0.8f);
                                        checkSectionClosingBackground.setBackgroundResource(R.color.colorGray);
                                        checkSectionClosingToastMakerView.setVisibility(View.VISIBLE);
                                        checkSectionClosingToastMakerView.setTag(TIME_AGO);
                                    }
                                }
                            } else {
                                checkSectionClosingBackground.setAlpha(0.3f);
                                checkSectionClosingToastMakerView.setVisibility(View.VISIBLE);
                                checkSectionClosingToastMakerView.setTag(IS_DONE);

                                checkSectionClosing.setEnabled(false);
                                checkSectionClosing.setClickable(false);
                                checkSectionClosing.setFocusable(false);
                                checkSectionClosing.setAlpha(1f);
                                checkSectionClosingDoneImageView.setImageResource(R.drawable.ic_done_all_black_36dp);
                                checkSectionClosingDoneImageView.setVisibility(View.VISIBLE);
                                checkSectionClosingBackground.setBackgroundResource(android.R.color.holo_green_light);
                            }

                        }

                        loadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getUserInfo();
//                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getEquipmentDisposable != null && !getEquipmentDisposable.isDisposed()) {
            getEquipmentDisposable.dispose();
        }

        if (getUserInfoDisposable != null && !getUserInfoDisposable.isDisposed()) {
            getUserInfoDisposable.dispose();
        }

        if (userLogoutDisposable != null && !userLogoutDisposable.isDisposed()) {
            userLogoutDisposable.dispose();
        }

        if (getLinksDisposable != null && !getLinksDisposable.isDisposed()) {
            getLinksDisposable.dispose();
        }

        if (sendOneSignalPlayerIDToServerDisposable != null && !sendOneSignalPlayerIDToServerDisposable.isDisposed()) {
            sendOneSignalPlayerIDToServerDisposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
}
