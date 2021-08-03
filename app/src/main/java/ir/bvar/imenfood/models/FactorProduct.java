package ir.bvar.imenfood.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class FactorProduct implements Serializable {
    private long id;
    private Product selectedProduct;
    private Provider selectedProvider;

    private List<Product> loadedProductList = new ArrayList<>();

    public FactorProduct() {
        this.id = System.currentTimeMillis();
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public Provider getSelectedProvider() {
        return selectedProvider;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public void setSelectedProvider(Provider selectedProvider) {
        this.selectedProvider = selectedProvider;
    }

    public void setLoadedProductList(List<Product> loadedProductList) {
        if (loadedProductList != null && loadedProductList.size() > 0) {
            this.loadedProductList.clear();
            this.loadedProductList.addAll(loadedProductList);
        }
    }

    public List<Product> getLoadedProductList() {
        return loadedProductList;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactorProduct)) return false;

        FactorProduct that = (FactorProduct) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
