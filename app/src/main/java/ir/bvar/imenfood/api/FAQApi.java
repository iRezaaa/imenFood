package ir.bvar.imenfood.api;

import java.util.List;

import io.reactivex.Observable;
import ir.bvar.imenfood.models.FAQ;
import retrofit2.http.GET;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public interface FAQApi {

    @GET("faq/")
    Observable<List<FAQ>> getFAQList();
}
