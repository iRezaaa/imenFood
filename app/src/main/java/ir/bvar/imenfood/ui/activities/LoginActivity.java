package ir.bvar.imenfood.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import ir.bvar.imenfood.api.response.ErrorResponse;
import ir.bvar.imenfood.api.response.LoginResponse;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.activity_login_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.activity_login_userEditText)
    AppCompatEditText userEditText;

    @BindView(R.id.activity_login_passwordEditText)
    AppCompatEditText passwordEditText;

    @BindView(R.id.activity_login_processButton)
    AppCompatButton signInButton;

    @BindView(R.id.activity_login_signUpButton)
    AppCompatButton signUpButton;

    @BindView(R.id.activity_login_forgetPasswordTextView)
    AppCompatTextView forgetPasswordTextViewButton;

    private Disposable loginRequestDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        forgetPasswordTextViewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_processButton:
                checkupAndSend();
                break;
            case R.id.activity_login_signUpButton:
                SignupActivity.start(this);
                break;
            case R.id.activity_login_forgetPasswordTextView:
                break;
        }
    }

    private void checkupAndSend() {
        if (loginRequestDisposable != null && !loginRequestDisposable.isDisposed()) {
            loginRequestDisposable.dispose();
        }

        if (!userEditText.getText().toString().isEmpty()) {

            if (!passwordEditText.getText().toString().isEmpty()) {

                loadingLayout.setVisibility(View.VISIBLE);
                userEditText.setEnabled(false);
                passwordEditText.setEnabled(false);
                signInButton.setEnabled(false);


                loginRequestDisposable = App.getInstance()
                        .getRetrofitInstance()
                        .create(AuthApi.class)
                        .login(userEditText.getText().toString(), passwordEditText.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<LoginResponse>() {
                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                App.getInstance().getSharedPreferencesManagerInstance().setAuthToken(loginResponse.getToken());
                                MainActivity.start(LoginActivity.this);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e instanceof HttpException) {
                                    HttpException exception = (HttpException) e;
                                    Response response = exception.response();

                                    if (response != null && response.errorBody() != null) {
                                        try {
                                            ErrorResponse errorResponse = App.getInstance().getGsonInstance().fromJson(response.errorBody().string(), ErrorResponse.class);
                                            Toast.makeText(LoginActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                } else {
                                    e.printStackTrace();
                                }

                                loadingLayout.setVisibility(View.GONE);
                                userEditText.setEnabled(true);
                                passwordEditText.setEnabled(true);
                                signInButton.setEnabled(true);
                            }

                            @Override
                            public void onComplete() {
                                loadingLayout.setVisibility(View.GONE);
                                userEditText.setEnabled(true);
                                passwordEditText.setEnabled(true);
                                signInButton.setEnabled(true);
                            }
                        });
            } else {
                Toast.makeText(this, "لطفا رمز عبور را وارد کنید", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "لطفا نام کاربری را وارد کنید", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginRequestDisposable != null && !loginRequestDisposable.isDisposed()) {
            loginRequestDisposable.dispose();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }
}
