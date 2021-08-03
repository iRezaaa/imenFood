package ir.bvar.imenfood.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ir.bvar.imenfood.models.Product;
import ir.bvar.imenfood.models.Provider;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class SendFactorRequest implements Serializable {

    @SerializedName("image")
    private String imagePath;

    @SerializedName("product_provider_data")
    private List<SendFactorProductProvider> productProviderList = new ArrayList<>();

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void addProduct(Product product, Provider provider) {
        SendFactorProductProvider productProvider = new SendFactorProductProvider();

        productProvider.setProductID(product.getID());
        productProvider.setProviderID(provider.getID());

        productProviderList.add(productProvider);
    }

    public List<SendFactorProductProvider> getProductProviderList() {
        return productProviderList;
    }

    public class SendFactorProductProvider {

        @SerializedName("provider")
        private int providerID;

        @SerializedName("product")
        private int productID;

        public void setProviderID(int providerID) {
            this.providerID = providerID;
        }

        public void setProductID(int productID) {
            this.productID = productID;
        }
    }
}
