package ir.bvar.imenfood.api;

import io.reactivex.Observable;
import ir.bvar.imenfood.api.response.GetLinksResponse;
import retrofit2.http.GET;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public interface LinksApi {

    @GET("links/")
    Observable<GetLinksResponse> getLinks();
}
