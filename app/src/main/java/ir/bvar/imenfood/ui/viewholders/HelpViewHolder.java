package ir.bvar.imenfood.ui.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.FAQ;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class HelpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;

    @BindView(R.id.viewholder_help_mainCardView)
    CardView mainCardView;

    @BindView(R.id.viewholder_help_titleTextView)
    AppCompatTextView titleTextView;

    @BindView(R.id.viewholder_help_bodyTextView)
    AppCompatTextView bodyTextView;

    private OnHelpItemClickListener onHelpItemClickListener;
    private FAQ currentItem;

    public HelpViewHolder(Context context, ViewGroup parent, OnHelpItemClickListener onHelpItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_help, parent, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.onHelpItemClickListener = onHelpItemClickListener;
    }

    public void bindViews(FAQ FAQ) {
        currentItem = FAQ;

        itemView.setOnClickListener(this);

        if (!FAQ.isExpanded()) {
            mainCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            titleTextView.setTypeface(titleTextView.getTypeface(),Typeface.NORMAL);
            titleTextView.setTextColor(Color.WHITE);
            bodyTextView.setVisibility(View.GONE);
        } else {
            mainCardView.setCardBackgroundColor(Color.WHITE);
            titleTextView.setTypeface(titleTextView.getTypeface(),Typeface.BOLD);
            titleTextView.setTextColor(context.getResources().getColor(R.color.solid));
            bodyTextView.setVisibility(View.VISIBLE);
            bodyTextView.setText(FAQ.getAnswer());
        }

        titleTextView.setText(FAQ.getQuestion());
    }

    @Override
    public void onClick(View view) {
        if (onHelpItemClickListener != null) {
            onHelpItemClickListener.onHelpItemClick(currentItem);
        }
    }

    public interface OnHelpItemClickListener {
        void onHelpItemClick(FAQ FAQ);
    }
}
