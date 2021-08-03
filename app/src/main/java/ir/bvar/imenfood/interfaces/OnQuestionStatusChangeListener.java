package ir.bvar.imenfood.interfaces;

import ir.bvar.imenfood.models.Question;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public interface OnQuestionStatusChangeListener {
    void onQuestionDone(int position, Question question);
    void onQuestionEdit(int position,Question question);
}
