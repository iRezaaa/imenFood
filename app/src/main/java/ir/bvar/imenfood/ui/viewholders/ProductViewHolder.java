package ir.bvar.imenfood.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.Product;
import ir.bvar.imenfood.ui.adapters.ProductListAdapter;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private List<Product> productList;
    private ProductListAdapter.ProductListAdapterClickListener productListAdapterClickListener;


    @BindView(R.id.viewholder_provider_mainRadioButton)
    AppCompatRadioButton mainRadioButton;

    @BindView(R.id.viewholder_provider_nameTextView)
    AppCompatTextView nameTextView;

    public ProductViewHolder(Context context, ViewGroup parent, List<Product> productList, ProductListAdapter.ProductListAdapterClickListener productListAdapterClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_provider, parent, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.productList = productList;
        this.productListAdapterClickListener = productListAdapterClickListener;
        itemView.setOnClickListener(this);
        mainRadioButton.setOnClickListener(this);
    }

    public void bindViews(int position) {
        Product product = productList.get(position);

        if (product != null) {
            nameTextView.setText(product.getName());
            mainRadioButton.setChecked(product.isChecked());
        }
    }

    @Override
    public void onClick(View view) {
        sendCallback();
    }

    private void sendCallback() {
        int adapterPosition = getAdapterPosition();

        if (adapterPosition != -1) {
            Product product = productList.get(adapterPosition);

            if (product != null) {
                if (productListAdapterClickListener != null) {
                    productListAdapterClickListener.onProductClick(product, adapterPosition);
                }
            }
        }
    }
}
