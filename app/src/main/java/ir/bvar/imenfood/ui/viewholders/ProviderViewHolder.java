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
import ir.bvar.imenfood.models.Provider;
import ir.bvar.imenfood.ui.adapters.ProviderListAdapter;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private List<Provider> providerList;
    private ProviderListAdapter.ProviderListAdapterClickListener providerListAdapterClickListener;


    @BindView(R.id.viewholder_provider_mainRadioButton)
    AppCompatRadioButton mainRadioButton;

    @BindView(R.id.viewholder_provider_nameTextView)
    AppCompatTextView nameTextView;

    public ProviderViewHolder(Context context, ViewGroup parent, List<Provider> providerList, ProviderListAdapter.ProviderListAdapterClickListener providerListAdapterClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_provider, parent, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.providerList = providerList;
        this.providerListAdapterClickListener = providerListAdapterClickListener;
        itemView.setOnClickListener(this);
        mainRadioButton.setOnClickListener(this);
    }

    public void bindViews(int position) {
        Provider provider = providerList.get(position);

        if (provider != null) {
            nameTextView.setText(provider.getName());
            mainRadioButton.setChecked(provider.isChecked());
        }
    }

    @Override
    public void onClick(View view) {
        sendCallback();
    }

    private void sendCallback() {
        int adapterPosition = getAdapterPosition();

        if (adapterPosition != -1) {
            Provider provider = providerList.get(adapterPosition);

            if (provider != null) {
                if (providerListAdapterClickListener != null) {
                    providerListAdapterClickListener.onProviderClick(provider, adapterPosition);
                }
            }
        }
    }
}
