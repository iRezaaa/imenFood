package ir.bvar.imenfood.api;

import java.util.List;

import io.reactivex.Observable;
import ir.bvar.imenfood.api.request.GetProductRequest;
import ir.bvar.imenfood.api.request.SendFactorRequest;
import ir.bvar.imenfood.api.response.GetProductsResponse;
import ir.bvar.imenfood.api.response.SendFactorResponse;
import ir.bvar.imenfood.api.response.UploadFactorImageResponse;
import ir.bvar.imenfood.models.Provider;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public interface FactorApi {

    @GET("provider/")
    Observable<List<Provider>> getProviders();

    @POST("products/")
    Observable<GetProductsResponse> getProducts(@Body GetProductRequest request);

    @POST("upload/")
    @Multipart
    Observable<UploadFactorImageResponse> uploadFactorImage(@Part MultipartBody.Part file);

    @POST("receipt/")
    Observable<SendFactorResponse> sendFactor(@Body SendFactorRequest sendFactorRequest);
}
