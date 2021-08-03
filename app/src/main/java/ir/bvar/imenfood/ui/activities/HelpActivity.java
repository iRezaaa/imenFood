package ir.bvar.imenfood.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.bvar.imenfood.App;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.api.FAQApi;
import ir.bvar.imenfood.models.FAQ;
import ir.bvar.imenfood.ui.adapters.HelpListAdapter;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class HelpActivity extends BaseActivity {

    @BindView(R.id.activity_help_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.toolbar_activity_help_backButton)
    AppCompatImageView backButton;

    @BindView(R.id.activity_help_mainRecyclerView)
    RecyclerView mainRecyclerView;
    private HelpListAdapter helpListAdapter;
    private List<FAQ> FAQList = new ArrayList<>();

    private Disposable getFAQListDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (helpListAdapter == null) {
            helpListAdapter = new HelpListAdapter(this, FAQList);
            helpListAdapter.setHasStableIds(true);
        }

        if (mainRecyclerView.getAdapter() == null) {
            mainRecyclerView.setAdapter(helpListAdapter);
        }

        if (mainRecyclerView.getLayoutManager() == null) {
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        updateData();
    }

    @Override
    public void updateData() {
        super.updateData();
        getFAQListDisposable = App.getInstance()
                .getRetrofitInstance().create(FAQApi.class)
                .getFAQList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<FAQ>>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadingLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<FAQ> faqs) {
                        loadingLayout.setVisibility(View.GONE);
                        if (faqs != null) {
                            FAQList.clear();
                            FAQList.addAll(faqs);
                            helpListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingLayout.setVisibility(View.GONE);
                        Toast.makeText(HelpActivity.this, "Error while getting faqs , please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getFAQListDisposable != null && !getFAQListDisposable.isDisposed()) {
            getFAQListDisposable.dispose();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, HelpActivity.class);
        context.startActivity(starter);
    }
}
