package ir.bvar.imenfood.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.FactorProduct;
import ir.bvar.imenfood.models.Product;
import ir.bvar.imenfood.ui.adapters.ProductListAdapter;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class ProductsDialog extends AppCompatDialog implements ProductListAdapter.ProductListAdapterClickListener {

    @BindView(R.id.dialog_products_mainRecyclerView)
    RecyclerView mainRecyclerView;
    private ProductListAdapter productListAdapter;
    private FactorProduct requestedFactorProduct;
    private List<Product> productList = new ArrayList<>();

    private ProductDialogSelectItemListener productDialogSelectItemListener;

    public ProductsDialog(Context context) {
        super(context);
    }

    public ProductsDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void show(FactorProduct factorProduct, ProductDialogSelectItemListener providerDialogSelectItemListener) {
        super.show();
        this.productDialogSelectItemListener = providerDialogSelectItemListener;
        this.requestedFactorProduct = factorProduct;
        this.productList.clear();
        this.productList.addAll(factorProduct.getLoadedProductList());

        if (productListAdapter != null)
            mainRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    notifyAdapterDataChanged();
                }
            });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        requestedFactorProduct = null;
        productList.clear();

        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyAdapterDataChanged();
            }
        });
    }

    private void init() {
        setContentView(R.layout.dialog_products);
        ButterKnife.bind(this);

        if (productListAdapter == null) {
            productListAdapter = new ProductListAdapter(getContext(), productList, this);
        }

        if (mainRecyclerView.getAdapter() == null) {
            mainRecyclerView.setAdapter(productListAdapter);
        }

        if (mainRecyclerView.getLayoutManager() == null) {
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public void notifyAdapterDataChanged() {
        if (productListAdapter != null) {
            productListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onProductClick(Product product, int position) {
        for (Product productInList : productList) {
            productInList.setChecked(false);
        }

        product.setChecked(true);

        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyAdapterDataChanged();
            }
        });

        if (productDialogSelectItemListener != null) {
            productDialogSelectItemListener.onProductDialogSelectedItemListener(product, requestedFactorProduct);
        }
    }


    public interface ProductDialogSelectItemListener {
        void onProductDialogSelectedItemListener(Product product, FactorProduct requestedFactorProduct);
    }
}
