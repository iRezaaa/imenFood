package ir.bvar.imenfood.api;

import java.util.List;

import io.reactivex.Observable;
import ir.bvar.imenfood.api.response.AnswerResponse;
import ir.bvar.imenfood.enums.CheckUpTypeEnum;
import ir.bvar.imenfood.models.Answer;
import ir.bvar.imenfood.models.CheckupTimes;
import ir.bvar.imenfood.models.Question;
import ir.bvar.imenfood.models.QuestionFeed;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public interface QuestionAnswerApi {

    @GET("questions/")
    Observable<List<Question>> getQuestions(@Query("section") CheckUpTypeEnum checkUpTypeEnum);

    @GET("question_feed/")
    Observable<List<QuestionFeed>> getQuestionFeed();

    @POST("answers/")
    @Headers("Content-Type: application/json")
    Observable<AnswerResponse> answerToQuestion(@Body List<Answer> body);

    @GET("hours/")
    Observable<CheckupTimes> getCheckupTimes();
}
