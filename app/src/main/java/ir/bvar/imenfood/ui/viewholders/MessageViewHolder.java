package ir.bvar.imenfood.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.Message;
import ir.bvar.imenfood.ui.views.VerticalTextView;
import ir.bvar.imenfood.utils.TimeUtility;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.viewholder_message_titleTextView)
    AppCompatTextView titleTextView;

    @BindView(R.id.viewholder_message_bodyTextView)
    AppCompatTextView bodyTextView;

    @BindView(R.id.viewholder_message_timeTextView)
    VerticalTextView timeTextView;

    public MessageViewHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_message, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public void bindViews(Message message) {
        int adapterPosition = getAdapterPosition();
        titleTextView.setText(message.getTitle());
        bodyTextView.setText(message.getContent());

        DateTime calendar = TimeUtility.getTimeFromBullSheetServerString(message.getDate(),message.getTime());

        if(calendar != null){
            String periodTime = TimeUtility.getPeriodTextFromCalendar(calendar);

            if(periodTime != null){
                timeTextView.setVisibility(View.VISIBLE);
                timeTextView.setText(periodTime);
            }else{
                timeTextView.setVisibility(View.GONE);
            }
        }else{
            timeTextView.setVisibility(View.GONE);
        }
    }
}
