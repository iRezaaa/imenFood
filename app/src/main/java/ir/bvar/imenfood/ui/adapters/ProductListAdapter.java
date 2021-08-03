package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.models.Product;
import ir.bvar.imenfood.ui.viewholders.ProductViewHolder;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private ProductListAdapterClickListener productListAdapterClickListener;

    public ProductListAdapter(Context context, List<Product> productList, ProductListAdapterClickListener productListAdapterClickListener) {
        this.context = context;
        this.productList = productList;
        this.productListAdapterClickListener = productListAdapterClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(context, parent, productList, productListAdapterClickListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ProductListAdapterClickListener {
        void onProductClick(Product product, int position);
    }
}
