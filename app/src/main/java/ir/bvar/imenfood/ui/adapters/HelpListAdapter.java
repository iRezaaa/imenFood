package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.models.FAQ;
import ir.bvar.imenfood.ui.viewholders.HelpViewHolder;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class HelpListAdapter extends RecyclerView.Adapter<HelpViewHolder> implements HelpViewHolder.OnHelpItemClickListener {
    private Context context;
    private List<FAQ> FAQList;

    public HelpListAdapter(Context context, List<FAQ> FAQList) {
        this.context = context;
        this.FAQList = FAQList;
    }

    @Override
    public HelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HelpViewHolder(context, parent, this);
    }

    @Override
    public void onBindViewHolder(HelpViewHolder holder, int position) {
        if (position != -1) {
            FAQ FAQ = FAQList.get(position);

            if (FAQ != null) {
                holder.bindViews(FAQ);
            }
        }
    }

    @Override
    public int getItemCount() {
        return FAQList.size();
    }

    @Override
    public long getItemId(int position) {
        return FAQList.get(position).getID();
    }

    @Override
    public void onHelpItemClick(FAQ FAQ) {
        for (FAQ openedHelps : FAQList) {
            if (openedHelps != null && openedHelps.isExpanded()) {
                int openedIndex = FAQList.indexOf(openedHelps);

                if (openedIndex != -1) {
                    openedHelps.setExpanded(false);
                    notifyItemChanged(openedIndex);
                }
            }
        }

        int index = FAQList.indexOf(FAQ);

        if (index != -1) {
            FAQ.setExpanded(!FAQ.isExpanded());
            notifyItemChanged(index);
        }
    }
}
