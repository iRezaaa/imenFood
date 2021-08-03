package ir.bvar.imenfood.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
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
import ir.bvar.imenfood.api.NotificationApi;
import ir.bvar.imenfood.models.Message;
import ir.bvar.imenfood.ui.adapters.MessageListAdapter;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.activity_messagelist_emptyHolder)
    AppCompatTextView emptyHolder;

    @BindView(R.id.activity_messagelist_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.toolbar_activity_messagelist_backButton)
    AppCompatImageView backButton;

    @BindView(R.id.activity_messagelist_mainRecyclerView)
    RecyclerView mainRecyclerView;

    private MessageListAdapter messageListAdapter;

    List<Message> messageList = new ArrayList<>();

    private Disposable getMessagesDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);
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

        if (messageListAdapter == null) {
            messageListAdapter = new MessageListAdapter(this, messageList);
            messageListAdapter.setHasStableIds(true);
        }

        if (mainRecyclerView.getAdapter() == null) {
            mainRecyclerView.setAdapter(messageListAdapter);
        }

        if (mainRecyclerView.getLayoutManager() == null) {
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        updateData();
    }

    @Override
    public void updateData() {
        super.updateData();
        getMessagesDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(NotificationApi.class)
                .getMessageList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Message>>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadingLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Message> messages) {

                        loadingLayout.setVisibility(View.GONE);
                        messageList.clear();
                        messageList.addAll(messages);
                        messageListAdapter.notifyDataSetChanged();

                        if (messageList.size() <= 0) {
                            mainRecyclerView.setVisibility(View.GONE);
                            emptyHolder.setVisibility(View.VISIBLE);
                        } else {
                            mainRecyclerView.setVisibility(View.VISIBLE);
                            emptyHolder.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingLayout.setVisibility(View.GONE);
                        Toast.makeText(MessageListActivity.this, "Error while getting notification , please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getMessagesDisposable != null && !getMessagesDisposable.isDisposed()) {
            getMessagesDisposable.dispose();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MessageListActivity.class);
        context.startActivity(starter);
    }
}
