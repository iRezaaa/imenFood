package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.interfaces.SendFactorRequestDialogListener;
import ir.bvar.imenfood.models.FactorProduct;
import ir.bvar.imenfood.ui.viewholders.FactorProductViewHolder;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class FactorProductListAdapter extends RecyclerView.Adapter<FactorProductViewHolder> {
    private Context context;
    private List<FactorProduct> factorProductList;
    private SendFactorRequestDialogListener sendFactorRequestDialogListener;

    public FactorProductListAdapter(Context context, List<FactorProduct> factorProductList, SendFactorRequestDialogListener sendFactorRequestDialogListener) {
        this.context = context;
        this.factorProductList = factorProductList;
        this.sendFactorRequestDialogListener = sendFactorRequestDialogListener;
    }

    @Override
    public FactorProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FactorProductViewHolder(context, parent, factorProductList, sendFactorRequestDialogListener);
    }

    @Override
    public void onBindViewHolder(FactorProductViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return factorProductList.size();
    }

    @Override
    public long getItemId(int position) {
        return factorProductList.get(position).getId();
    }
}
