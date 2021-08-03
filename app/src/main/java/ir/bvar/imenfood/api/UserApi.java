package ir.bvar.imenfood.api;

import io.reactivex.Observable;
import ir.bvar.imenfood.models.UserInfo;
import retrofit2.http.GET;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public interface UserApi {

    @GET("my_user/")
    Observable<UserInfo> getUserInfo();
}
