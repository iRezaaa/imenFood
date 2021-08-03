package ir.bvar.imenfood.api;

import io.reactivex.Observable;
import ir.bvar.imenfood.api.request.SignupRequest;
import ir.bvar.imenfood.api.response.LogOutResponse;
import ir.bvar.imenfood.api.response.LoginResponse;
import ir.bvar.imenfood.api.response.SignupResponse;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by rezapilehvar on 28/12/2017 AD.
 */

public interface AuthApi {

    @POST("pending/")
    Observable<Result<SignupResponse>> signup(@Body SignupRequest signupRequest);

    @POST("login/")
    @FormUrlEncoded
    Observable<LoginResponse> login(@Field("user_name") String username, @Field("password") String password);

    @GET("logout/")
    Observable<LogOutResponse> logout();
}
