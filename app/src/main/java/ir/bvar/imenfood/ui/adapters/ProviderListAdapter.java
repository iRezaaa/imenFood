package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.models.Provider;
import ir.bvar.imenfood.ui.viewholders.ProviderViewHolder;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class ProviderListAdapter extends RecyclerView.Adapter<ProviderViewHolder> {
    private Context context;
    private List<Provider> providerList;
    private ProviderListAdapterClickListener providerListAdapterClickListener;

    public ProviderListAdapter(Context context, List<Provider> providerList, ProviderListAdapterClickListener providerListAdapterClickListener) {
        this.context = context;
        this.providerList = providerList;
        this.providerListAdapterClickListener = providerListAdapterClickListener;
    }

    @Override
    public ProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProviderViewHolder(context, parent, providerList, providerListAdapterClickListener);
    }

    @Override
    public void onBindViewHolder(ProviderViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    public interface ProviderListAdapterClickListener {
        void onProviderClick(Provider provider, int position);
    }
}
