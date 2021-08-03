package ir.bvar.imenfood.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ir.bvar.imenfood.interfaces.OnQuestionStatusChangeListener;
import ir.bvar.imenfood.models.Question;
import ir.bvar.imenfood.models.QuestionFeed;
import ir.bvar.imenfood.ui.fragments.CheckUpFragment;

/**
 * Created by rezapilehvar on 24/12/2017 AD.
 */

public class CheckUpFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Question> questionList;
    private List<QuestionFeed> questionFeedList;
    private OnQuestionStatusChangeListener questionDoneListener;

    public CheckUpFragmentPagerAdapter(FragmentManager fm, List<Question> questionList, List<QuestionFeed> questionFeedList, OnQuestionStatusChangeListener questionDoneListener) {
        super(fm);
        this.questionList = questionList;
        this.questionFeedList = questionFeedList;
        this.questionDoneListener = questionDoneListener;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Fragment getItem(int position) {
        CheckUpFragment checkUpFragment = new CheckUpFragment();
        checkUpFragment.setCheckup(questionFeedList,questionList, position);
        checkUpFragment.setQuestionStatusChangeListener(questionDoneListener);
        return checkUpFragment;
    }
}
