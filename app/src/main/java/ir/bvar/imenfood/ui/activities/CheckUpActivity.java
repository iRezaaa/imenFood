package ir.bvar.imenfood.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.duolingo.open.rtlviewpager.RtlViewPager;

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
import ir.bvar.imenfood.api.QuestionAnswerApi;
import ir.bvar.imenfood.api.response.AnswerResponse;
import ir.bvar.imenfood.constants.IntentKeys;
import ir.bvar.imenfood.enums.CheckUpTypeEnum;
import ir.bvar.imenfood.interfaces.OnQuestionStatusChangeListener;
import ir.bvar.imenfood.models.Question;
import ir.bvar.imenfood.models.QuestionFeed;
import ir.bvar.imenfood.models.SharedPreferencesCheckup;
import ir.bvar.imenfood.ui.adapters.CheckUpFragmentPagerAdapter;
import ir.bvar.imenfood.utils.CollectionsUtility;
import ir.bvar.imenfood.utils.TimeUtility;
import ir.bvar.imenfood.utils.ViewUtility;

/**
 * Created by rezapilehvar on 24/12/2017 AD.
 */

public class CheckUpActivity extends BaseActivity implements OnQuestionStatusChangeListener, View.OnClickListener {
    private CheckUpTypeEnum checkUpType;

    List<Question> questionList = new ArrayList<>();
    List<QuestionFeed> questionFeedList = new ArrayList<>();
    CheckUpFragmentPagerAdapter checkUpFragmentPagerAdapter;

    @BindView(R.id.activity_checkup_mainCoordinatorLayout)
    CoordinatorLayout mainCoordinatorLayout;

    @BindView(R.id.toolbar_activity_checkup_titleTextView)
    AppCompatTextView titleTextView;

    @BindView(R.id.toolbar_activity_checkup_backButton)
    AppCompatImageView backButton;

    @BindView(R.id.activity_checkup_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.activity_checkup_mainViewPager)
    RtlViewPager mainViewPager;

    private Snackbar sendAnswersSnackBar;
    private Snackbar retrySendAnswersSnackBar;

    private Disposable getQuestionsDisposable;
    private Disposable getQuestionFeedDisposable;
    private Disposable sendAnswersDisposable;

    private boolean isSent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        checkUpType = (CheckUpTypeEnum) getIntent().getSerializableExtra(IntentKeys.KEY_CHECKUP_TYPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);
        ButterKnife.bind(this);

        if (sendAnswersSnackBar == null) {
            sendAnswersSnackBar = Snackbar.make(mainCoordinatorLayout, "جواب ها به سرور ارسال شوند؟", BaseTransientBottomBar.LENGTH_INDEFINITE);
            sendAnswersSnackBar.setAction("ارسال", this);
            ViewUtility.disableSwipeOnSnackbar(sendAnswersSnackBar);
        }

        if (retrySendAnswersSnackBar == null) {
            retrySendAnswersSnackBar = Snackbar.make(mainCoordinatorLayout, "خطا هنگام ارسال جواب ها ، دوباره امتحان کنید", BaseTransientBottomBar.LENGTH_INDEFINITE);
            retrySendAnswersSnackBar.setAction("تلاش مجدد", this);
            ViewUtility.disableSwipeOnSnackbar(retrySendAnswersSnackBar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View view1 = sendAnswersSnackBar.getView();
            View view2 = retrySendAnswersSnackBar.getView();
            view1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            view2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        // check saved questions
        SharedPreferencesCheckup sharedPreferencesCheckup = App.getInstance().getSharedPreferencesManagerInstance().getSavedCheckup(checkUpType);
        if (sharedPreferencesCheckup != null && TimeUtility.isToday(sharedPreferencesCheckup.getTimestamp())) {
            questionList.clear();
            questionList.addAll(sharedPreferencesCheckup.getCheckUpList());

            checkDone();

            loadingLayout.setVisibility(View.GONE);
        } else {
            updateData();
        }

        initViews();
    }

    private void initViews() {

        switch (checkUpType) {
            case Morning:
                titleTextView.setText("چک کردن بازگشایی");
                break;
            case During:
                titleTextView.setText("چک کردن مخصوص روز");
                break;
            case Closing:
                titleTextView.setText("چک کردن هنگام تعطیلی");
                break;
        }
        if (checkUpFragmentPagerAdapter == null) {
            checkUpFragmentPagerAdapter = new CheckUpFragmentPagerAdapter(getSupportFragmentManager(), questionList, questionFeedList, this);
        }

        if (mainViewPager.getAdapter() == null) {
            mainViewPager.setAdapter(checkUpFragmentPagerAdapter);
        }

        mainViewPager.setOffscreenPageLimit(20);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void updateData() {
        super.updateData();
        getQuestions();
    }

    private void getQuestions() {
        getQuestionsDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(QuestionAnswerApi.class)
                .getQuestions(checkUpType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Question>>() {
                    @Override
                    public void onNext(List<Question> questionList) {
                        CheckUpActivity.this.questionList.clear();
                        CheckUpActivity.this.questionList.addAll(questionList);
                        getQuestionFeed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        Toast.makeText(CheckUpActivity.this, "Error while getting checkup list , please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getQuestionFeed() {
        getQuestionFeedDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(QuestionAnswerApi.class)
                .getQuestionFeed()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<QuestionFeed>>() {
                    @Override
                    public void onNext(List<QuestionFeed> questionFeedList) {
                        if (checkUpFragmentPagerAdapter != null && questionFeedList != null) {
                            CheckUpActivity.this.questionFeedList.clear();
                            CheckUpActivity.this.questionFeedList.addAll(questionFeedList);
                            checkUpFragmentPagerAdapter.notifyDataSetChanged();
                            loadingLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CheckUpActivity.this, "Error while getting checkup list , please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getQuestionsDisposable != null && !getQuestionsDisposable.isDisposed()) {
            getQuestionsDisposable.dispose();
        }

        if (getQuestionFeedDisposable != null && !getQuestionFeedDisposable.isDisposed()) {
            getQuestionFeedDisposable.dispose();
        }

        if (sendAnswersDisposable != null && !sendAnswersDisposable.isDisposed()) {
            sendAnswersDisposable.dispose();
        }

        if (!isSent) {
            App.getInstance().getSharedPreferencesManagerInstance().saveCheckUp(questionList, checkUpType);
        } else {
            App.getInstance().getSharedPreferencesManagerInstance().saveCheckUp(null, checkUpType);
        }
    }

    public static void start(Context context, CheckUpTypeEnum checkUpTypeEnum) {
        Intent starter = new Intent(context, CheckUpActivity.class);
        starter.putExtra(IntentKeys.KEY_CHECKUP_TYPE, checkUpTypeEnum);
        context.startActivity(starter);
    }

    @Override
    public void onQuestionDone(final int position, final Question question) {
        question.setDone(true);

        if (position < (questionList.size() - 1) && position >= 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mainViewPager != null) {
                        if (question.isDone())
                            mainViewPager.setCurrentItem(position + 1);
                    }
                }
            }, 1000);
        }

        checkDone();
    }

    private void checkDone() {
        boolean isDone = true;

        for (Question quests : questionList) {
            if (!quests.isDone()) {
                isDone = false;
            }
        }

        if (isDone) {
            sendAnswersSnackBar.show();
        } else {
            sendAnswersSnackBar.dismiss();
        }
    }

    @Override
    public void onQuestionEdit(int position, Question question) {
        if (sendAnswersSnackBar.isShown()) {
            sendAnswersSnackBar.dismiss();
        }

        if (retrySendAnswersSnackBar.isShown()) {
            retrySendAnswersSnackBar.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!isSent) {
            App.getInstance().getSharedPreferencesManagerInstance().saveCheckUp(questionList, checkUpType);
        } else {
            App.getInstance().getSharedPreferencesManagerInstance().saveCheckUp(null, checkUpType);
        }
    }

    @Override
    public void onClick(View view) {
        if (sendAnswersDisposable != null && !sendAnswersDisposable.isDisposed()) {
            sendAnswersDisposable.dispose();
        }

        sendAnswersDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(QuestionAnswerApi.class)
                .answerToQuestion(CollectionsUtility.convertQuestionsToAnswer(questionList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AnswerResponse>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadingLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(AnswerResponse answerResponse) {
                        retrySendAnswersSnackBar.dismiss();
                        loadingLayout.setVisibility(View.GONE);
                        isSent = true;
                        App.getInstance().getSharedPreferencesManagerInstance().saveCheckUp(null, checkUpType);

                        finish();
                        Toast.makeText(CheckUpActivity.this, "جواب ها با موفقیت ارسال شدند", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingLayout.setVisibility(View.GONE);
                        retrySendAnswersSnackBar.show();
                        isSent = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
