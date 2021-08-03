package ir.bvar.imenfood.api.request;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import ir.bvar.imenfood.App;
import ir.bvar.imenfood.models.Answer;

/**
 * Created by rezapilehvar on 18/1/2018 AD.
 */

public class AnswerRequest implements Serializable {
    private List<Answer> answerList;

    public AnswerRequest(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public String toString() {
        Type listType = new TypeToken<List<Answer>>() {
        }.getType();

        return App.getInstance().getGsonInstance().toJson(answerList, listType);
    }
}
