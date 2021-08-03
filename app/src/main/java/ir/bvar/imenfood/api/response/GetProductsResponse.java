package ir.bvar.imenfood.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ir.bvar.imenfood.models.Product;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class GetProductsResponse implements Serializable {
    @SerializedName("products")
    List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }
}
