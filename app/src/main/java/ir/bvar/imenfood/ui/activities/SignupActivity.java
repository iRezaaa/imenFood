package ir.bvar.imenfood.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.bvar.imenfood.App;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.api.AuthApi;
import ir.bvar.imenfood.api.LinksApi;
import ir.bvar.imenfood.api.request.SignupRequest;
import ir.bvar.imenfood.api.response.GetLinksResponse;
import ir.bvar.imenfood.api.response.SignupResponse;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public class SignupActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.activity_signup_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.toolbar_activity_signup_backButton)
    AppCompatImageView backButton;

    @BindView(R.id.activity_signup_fullnameEditText)
    AppCompatEditText fullnameEditText;

    @BindView(R.id.activity_signup_restaurantNameEditText)
    AppCompatEditText restaurantNameEditText;

    @BindView(R.id.activity_signup_restaurantAddressEditText)
    AppCompatEditText restaurantAddressEditText;

    @BindView(R.id.activity_signup_phoneNumberEditText)
    AppCompatEditText phoneNumberEditText;

    @BindView(R.id.activity_signup_emailEditText)
    AppCompatEditText emailEditText;

    @BindView(R.id.activity_signup_agreeCheckbox)
    AppCompatCheckBox agreeCheckbox;

    @BindView(R.id.activity_signup_processButton)
    AppCompatButton processButton;

    @BindView(R.id.activity_signup_termsLinkTextView)
    AppCompatTextView termsLinkTextView;

    private Disposable signUpProcessDisposable;
    private Disposable getLinksDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        backButton.setOnClickListener(this);
        processButton.setOnClickListener(this);
        termsLinkTextView.setOnClickListener(this);
        getLinks();
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
                        App.getInstance().getStaticDataManager().setRulesLink("http://imenfood.ir/terms");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkUpAndProcess() {
        if (signUpProcessDisposable != null && !signUpProcessDisposable.isDisposed()) {
            signUpProcessDisposable.dispose();
        }

        SignupRequest signupRequest = null;

        if (!fullnameEditText.getText().toString().isEmpty()
                && !restaurantNameEditText.getText().toString().isEmpty()
                && !restaurantAddressEditText.getText().toString().isEmpty()
                && !phoneNumberEditText.getText().toString().isEmpty()
                && !emailEditText.getText().toString().isEmpty()
                && agreeCheckbox.isChecked()) {

            signupRequest = new SignupRequest();
            signupRequest.setFullName(fullnameEditText.getText().toString());
            signupRequest.setRestaurantName(restaurantNameEditText.getText().toString());
            signupRequest.setAddress(restaurantAddressEditText.getText().toString());
            signupRequest.setPhoneNumber(phoneNumberEditText.getText().toString());
            signupRequest.setEmail(emailEditText.getText().toString());
        } else {
            Toast.makeText(this, "لطفا فرم را کامل پر کنید و چک قوانین و مقررات رو بزنید", Toast.LENGTH_SHORT).show();
        }

        if (signupRequest != null) {
            signUpProcessDisposable = App.getInstance()
                    .getRetrofitInstance()
                    .create(AuthApi.class)
                    .signup(signupRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Result<SignupResponse>>() {
                        @Override
                        protected void onStart() {
                            super.onStart();
                            loadingLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(Result<SignupResponse> signupResponse) {
                            if (!signupResponse.isError() && signupResponse.response() != null) {
                                if (signupResponse.response().isSuccessful()) {
                                    finish();
                                    Toast.makeText(SignupActivity.this, "اطلاعات شما ارسال شد ، به زودی با شما تماس خواهیم گرفت", Toast.LENGTH_LONG).show();
                                } else {
                                    if (signupResponse.response().errorBody() != null) {
                                        try {
                                            SignupResponse.Error errorResponse = App.getInstance().getGsonInstance().fromJson(signupResponse.response().errorBody().string(), SignupResponse.Error.class);

                                            if (errorResponse.getEmailErrors() != null && errorResponse.getEmailErrors().size() > 0) {
                                                Toast.makeText(SignupActivity.this, "ایمیل وارد شده قبلا در سیستم ثبت شده ، لطفا با ایمیل دیگری امتحان کنید.", Toast.LENGTH_LONG).show();
                                            }

                                            if (errorResponse.getRestaurnalError() != null && errorResponse.getRestaurnalError().size() > 0) {
                                                Toast.makeText(SignupActivity.this, "اسم این رستوران قبلا در سیستم ثبت شده است.", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(SignupActivity.this, "خطا هنگام ثبت اطلاعات لطفا فرم را دوباره بررسی و ارسال کنید", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(SignupActivity.this, "خطا هنگام ثبت اطلاعات لطفا فرم را دوباره بررسی و ارسال کنید", Toast.LENGTH_SHORT).show();
                            }
                            loadingLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingLayout.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "خطا هنگام ثبت اطلاعات لطفا فرم را دوباره بررسی و ارسال کنید", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void openRulesLink(){
        String url;
        if (App.getInstance().getStaticDataManager().getRulesLink() != null && !App.getInstance().getStaticDataManager().getRulesLink().isEmpty() ){
             url = App.getInstance().getStaticDataManager().getRulesLink();
        }else{
            url = "http://imenfood.ir/terms";
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_activity_signup_backButton:
                onBackPressed();
                break;
            case R.id.activity_signup_processButton:
                checkUpAndProcess();
                break;
            case R.id.activity_signup_termsLinkTextView:
                openRulesLink();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (signUpProcessDisposable != null && !signUpProcessDisposable.isDisposed()) {
            signUpProcessDisposable.dispose();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SignupActivity.class);
        context.startActivity(starter);
    }
}
