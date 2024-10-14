package com.example.chatsocketnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messageList;

    public RecyclerViewAdapter(List<Message> messages) {
        this.messageList = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.isSentByUser() ? R.layout.chat_one_line_enviamsg : R.layout.chat_one_line_recebemsg;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        ((MessageViewHolder) holder).bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUsrMsg;
        public TextView textViewMsgRecebe;

        public MessageViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == R.layout.chat_one_line_enviamsg) {
                textViewUsrMsg = itemView.findViewById(R.id.textViewUsrMsg);
            } else if (viewType == R.layout.chat_one_line_recebemsg) {
                textViewMsgRecebe = itemView.findViewById(R.id.textViewMsgRecebe);
            }
        }

        public void bindMessage(Message message) {
            if (message.isSentByUser() && textViewUsrMsg != null) {
                textViewUsrMsg.setText(message.getContent());
            } else if (!message.isSentByUser() && textViewMsgRecebe != null) {
                textViewMsgRecebe.setText(message.getContent());
            }
        }
    }
}
