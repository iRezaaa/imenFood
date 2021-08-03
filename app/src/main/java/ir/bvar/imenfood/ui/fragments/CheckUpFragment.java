package ir.bvar.imenfood.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.enums.QuestionTypeEnum;
import ir.bvar.imenfood.interfaces.OnQuestionStatusChangeListener;
import ir.bvar.imenfood.models.Equipment;
import ir.bvar.imenfood.models.Question;
import ir.bvar.imenfood.models.QuestionFeed;
import ir.bvar.imenfood.ui.adapters.DeviceAdviceListAdapter;
import ir.bvar.imenfood.ui.adapters.DeviceListAdapter;

/**
 * Created by rezapilehvar on 24/12/2017 AD.
 */

public class CheckUpFragment extends BaseFragment {
    private Question question;
    private int checkupPosition;
    private int questionsCount;

    private OnQuestionStatusChangeListener questionStatusChangeListener;

    @BindView(R.id.fragment_checkup_item_mainFlipLayout)
    EasyFlipView mainFlipLayout;

    @BindView(R.id.fragment_checkup_item_titleTextView)
    AppCompatTextView titleTextView;

    @BindView(R.id.fragment_checkup_item_positionTextView)
    AppCompatTextView positionTextView;

    @BindView(R.id.fragment_checkup_item_itemsCountTextView)
    AppCompatTextView itemsCountTextView;

    @BindView(R.id.fragment_checkup_yesButton)
    CardView yesButton;

    @BindView(R.id.fragment_checkup_noButton)
    CardView noButton;

    @BindView(R.id.fragment_checkup_okButton)
    CardView okButton;

    @BindView(R.id.fragment_checkup_item_doneLayout)
    RelativeLayout doneLayout;

    @BindView(R.id.fragment_checkup_item_editButton)
    CardView editButton;

    @BindView(R.id.fragment_checkup_item_adviceContainer)
    RelativeLayout adviceContainer;

    @BindView(R.id.fragment_checkup_item_adviceTextView)
    AppCompatTextView adviceTextView;

    @BindView(R.id.fragment_checkup_item_deviceListRecyclerView)
    RecyclerView deviceListRecyclerView;
    private DeviceListAdapter deviceListAdapter;


    /* Back of the card */
    @BindView(R.id.fragment_checkup_item_back_backButton)
    AppCompatImageView backBackButton;

    @BindView(R.id.fragment_checkup_item_back_deviceAdviceRecyclerView)
    RecyclerView backDeviceAdviceRecyclerView;
    private DeviceAdviceListAdapter deviceAdviceListAdapter;
    private List<Equipment> deviceAdviceList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_checkup_item, container, false);
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public void setCheckup(List<QuestionFeed> questionFeedList, List<Question> questionList, int checkupPosition) {
        this.question = questionList.get(checkupPosition);
        this.checkupPosition = checkupPosition;
        this.questionsCount = questionList.size();

        for (QuestionFeed questionFeed : questionFeedList) {
            if (questionFeed.getQuestionID() == question.getID()) {
                question.setAdvice(questionFeed.getAdvice());
                question.setQuestionType(questionFeed.getQuestionType());
                question.setWhen(questionFeed.when());
            }
        }
    }

    public void setQuestionStatusChangeListener(OnQuestionStatusChangeListener questionStatusChangeListener) {
        this.questionStatusChangeListener = questionStatusChangeListener;

    }

    private void initViews() {
        titleTextView.setText(question.getText());

        if (question.getQuestionType() == QuestionTypeEnum.Device) {
            if (deviceListAdapter == null) {
                deviceListAdapter = new DeviceListAdapter(getContext());
                question.setDeviceList(deviceListAdapter.getEquipmentList());
            }

            if (deviceListRecyclerView.getAdapter() == null) {
                deviceListRecyclerView.setAdapter(deviceListAdapter);
            }

            if (deviceListRecyclerView.getLayoutManager() == null) {
                deviceListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            if (deviceAdviceListAdapter == null) {
                deviceAdviceListAdapter = new DeviceAdviceListAdapter(getContext(), deviceAdviceList);
            }

            if (backDeviceAdviceRecyclerView.getAdapter() == null) {
                backDeviceAdviceRecyclerView.setAdapter(deviceAdviceListAdapter);
            }

            if (backDeviceAdviceRecyclerView.getLayoutManager() == null) {
                backDeviceAdviceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }

        positionTextView.setText("" + (checkupPosition + 1));
        itemsCountTextView.setText("از " + questionsCount);

        checkDoneOrNot();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpAnswer(true);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpAnswer(false);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question.setDone(false);
                question.setFalseAnswerClicked(false);
                if (questionStatusChangeListener != null) {
                    questionStatusChangeListener.onQuestionEdit(checkupPosition, question);
                }
                checkDoneOrNot();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question.setDone(true);

                if (question.getQuestionType() == QuestionTypeEnum.Device) {

                    deviceAdviceList.clear();

                    boolean isChecked = false;

                    for (Equipment checkedEquipment : deviceListAdapter.getEquipmentList()) {
                        if (checkedEquipment.isChecked()) {
                            isChecked = true;
                            deviceAdviceList.add(checkedEquipment);
                        }
                    }

                    deviceAdviceListAdapter.notifyDataSetChanged();

                    if (isChecked) {
                        mainFlipLayout.flipTheView();
                        checkUpAnswer(question.when());
                    } else {
                        Toast.makeText(getContext(), "حداقل یکی از وسایل باید انتخاب شود", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    checkUpAnswer(question.when());
                }
            }
        });

        backBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainFlipLayout.flipTheView();
            }
        });

    }


    private void checkUpAnswer(boolean answer) {
        question.setAnswer(answer);

        if (question.when()) {
            if (!answer) {
                if (questionStatusChangeListener != null) {
                    questionStatusChangeListener.onQuestionDone(checkupPosition, question);
                }
                question.setFalseAnswerClicked(false);
            } else {
                question.setFalseAnswerClicked(true);

                if (question.isDone()) {
                    question.setFalseAnswerClicked(true);

                    if (questionStatusChangeListener != null) {
                        questionStatusChangeListener.onQuestionDone(checkupPosition, question);
                    }
                }
            }
        } else {
            if (answer) {
                if (questionStatusChangeListener != null) {
                    questionStatusChangeListener.onQuestionDone(checkupPosition, question);
                }

                question.setFalseAnswerClicked(false);
            } else {
                question.setFalseAnswerClicked(true);

                if (question.isDone()) {
                    if (questionStatusChangeListener != null) {
                        questionStatusChangeListener.onQuestionDone(checkupPosition, question);
                    }
                }
            }
        }

        checkDoneOrNot();
    }

    private void checkDoneOrNot() {
        if (question.isDone()) {
            doneLayout.setVisibility(View.VISIBLE);

            if (!question.isFalseAnswerClicked()) {
                if (question.when()) {
                    noButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                } else {
                    yesButton.setVisibility(View.VISIBLE);
                    noButton.setVisibility(View.GONE);
                }

                okButton.setVisibility(View.GONE);

                adviceContainer.setVisibility(View.GONE);
            } else {
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                okButton.setVisibility(View.VISIBLE);

                adviceContainer.setVisibility(View.VISIBLE);
                adviceTextView.setText(question.getAdvice());

                if (question.getQuestionType() == QuestionTypeEnum.Device) {
                    deviceListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    deviceListRecyclerView.setVisibility(View.GONE);
                }
            }

        } else {
            doneLayout.setVisibility(View.GONE);

            if (!question.isFalseAnswerClicked()) {
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.GONE);

                adviceContainer.setVisibility(View.GONE);

            } else {

                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                okButton.setVisibility(View.VISIBLE);

                adviceContainer.setVisibility(View.VISIBLE);
                adviceTextView.setText(question.getAdvice());

                if (question.getQuestionType() == QuestionTypeEnum.Device) {
                    deviceListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    deviceListRecyclerView.setVisibility(View.GONE);
                }
            }
        }
    }
}
