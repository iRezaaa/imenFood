package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.models.Message;
import ir.bvar.imenfood.ui.viewholders.MessageViewHolder;

/**
 * Created by rezapilehvar on 22/12/2017 AD.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private Context context;
    private List<Message> messageList;

    public MessageListAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (position != -1) {
            Message message = messageList.get(position);

            if (message != null) {
                holder.bindViews(message);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public long getItemId(int position) {
        return messageList.get(position).getID();
    }
}
