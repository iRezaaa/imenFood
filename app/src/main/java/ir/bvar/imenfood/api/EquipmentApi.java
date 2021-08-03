package ir.bvar.imenfood.api;

import java.util.List;

import io.reactivex.Observable;
import ir.bvar.imenfood.models.Equipment;
import retrofit2.http.GET;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public interface EquipmentApi {

    @GET("rest_equip/")
    Observable<List<Equipment>> getEquipmentList();
}
