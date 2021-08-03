package ir.bvar.imenfood.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.FactorProduct;
import ir.bvar.imenfood.models.Provider;
import ir.bvar.imenfood.ui.adapters.ProviderListAdapter;

/**
 * Created by rezapilehvar on 20/1/2018 AD.
 */

public class ProvidersDialog extends AppCompatDialog implements ProviderListAdapter.ProviderListAdapterClickListener {

    @BindView(R.id.dialog_providers_mainRecyclerView)
    RecyclerView mainRecyclerView;
    private List<Provider> providerList;
    private ProviderListAdapter providerListAdapter;
    private FactorProduct requestedFactorProduct;

    private ProviderDialogSelectItemListener providerDialogSelectItemListener;

    public ProvidersDialog(Context context, List<Provider> providerList) {
        super(context);
        this.providerList = providerList;
    }

    public ProvidersDialog(Context context, int theme, List<Provider> providerList) {
        super(context, theme);
        this.providerList = providerList;
    }

    protected ProvidersDialog(Context context, boolean cancelable, OnCancelListener cancelListener, List<Provider> providerList) {
        super(context, cancelable, cancelListener);
        this.providerList = providerList;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void show(FactorProduct factorProduct, ProviderDialogSelectItemListener providerDialogSelectItemListener) {
        super.show();
        this.providerDialogSelectItemListener = providerDialogSelectItemListener;
        this.requestedFactorProduct = factorProduct;

        for (Provider providerInList : providerList) {
            providerInList.setChecked(false);
        }

        if (factorProduct.getSelectedProvider() != null) {
            int index = providerList.indexOf(factorProduct.getSelectedProvider());

            if (index != -1)
                providerList.get(index).setChecked(true);
        }

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
        for (Provider providerInList : providerList) {
            providerInList.setChecked(false);
        }

        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyAdapterDataChanged();
            }
        });

        requestedFactorProduct = null;
    }

    private void init() {
        setContentView(R.layout.dialog_providers);
        ButterKnife.bind(this);

        if (providerListAdapter == null) {
            providerListAdapter = new ProviderListAdapter(getContext(), providerList, this);
        }

        if (mainRecyclerView.getAdapter() == null) {
            mainRecyclerView.setAdapter(providerListAdapter);
        }

        if (mainRecyclerView.getLayoutManager() == null) {
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public void notifyAdapterDataChanged() {
        if (providerListAdapter != null) {
            providerListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onProviderClick(Provider provider, int position) {
        for (Provider providerInList : providerList) {
            providerInList.setChecked(false);
        }

        provider.setChecked(true);

        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyAdapterDataChanged();
            }
        });

        if (providerDialogSelectItemListener != null) {
            providerDialogSelectItemListener.onProviderDialogSelectedItemListener(provider, requestedFactorProduct);
        }
    }


    public interface ProviderDialogSelectItemListener {
        void onProviderDialogSelectedItemListener(Provider provider, FactorProduct requestedFactorProduct);
    }
}
