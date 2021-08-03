package ir.bvar.imenfood.api;

import java.util.List;

import io.reactivex.Observable;
import ir.bvar.imenfood.api.request.SendPlayerIDRequest;
import ir.bvar.imenfood.api.response.SendPlayerIDResponse;
import ir.bvar.imenfood.models.Message;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public interface NotificationApi {

    @GET("notifications/")
    Observable<List<Message>> getMessageList();

    @POST("player_id/")
    Observable<SendPlayerIDResponse> sendOneSignalPlayerID(@Body SendPlayerIDRequest sendPlayerIDRequest);
}
