package ir.bvar.imenfood.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.interfaces.SendFactorRequestDialogListener;
import ir.bvar.imenfood.models.FactorProduct;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class FactorProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private List<FactorProduct> factorProductList;
    private SendFactorRequestDialogListener sendFactorRequestDialogListener;

    @BindView(R.id.viewholder_factorproduct_productCategoryTextView)
    AppCompatTextView providerTextView;

    @BindView(R.id.viewholder_factorproduct_productNameTextView)
    AppCompatTextView productTextView;

    public FactorProductViewHolder(Context context, ViewGroup parent, List<FactorProduct> factorProductList, SendFactorRequestDialogListener sendFactorRequestDialogListener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_factorproduct, parent, false));
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.factorProductList = factorProductList;
        this.sendFactorRequestDialogListener = sendFactorRequestDialogListener;
        providerTextView.setOnClickListener(this);
        productTextView.setOnClickListener(this);
    }

    public void bindViews(int position) {
        FactorProduct factorProduct = factorProductList.get(position);

        if (factorProduct.getSelectedProvider() != null) {
            providerTextView.setText(factorProduct.getSelectedProvider().getName());
        } else {
            providerTextView.setText("تهیه کننده");
        }

        if (factorProduct.getLoadedProductList().size() > 0) {
            productTextView.setEnabled(true);
        } else {
            productTextView.setEnabled(false);
        }

        if (factorProduct.getSelectedProduct() != null) {
            productTextView.setText(factorProduct.getSelectedProduct().getName());
        } else {
            productTextView.setText("نوع محصول");
        }
    }

    @Override
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();

        if (adapterPosition != -1) {
            FactorProduct factorProduct = factorProductList.get(adapterPosition);

            if (factorProduct != null && sendFactorRequestDialogListener != null) {
                switch (view.getId()) {
                    case R.id.viewholder_factorproduct_productCategoryTextView:
                        sendFactorRequestDialogListener.requestProvider(factorProduct, adapterPosition);
                        break;
                    case R.id.viewholder_factorproduct_productNameTextView:
                        if (productTextView.isEnabled()) {
                            sendFactorRequestDialogListener.requestProduct(factorProduct, adapterPosition);
                        }
                        break;
                }
            }
        }
    }
}
