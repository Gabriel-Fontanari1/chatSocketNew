package com.example.chatsocketnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ENVIADA = 1;
    private static final int VIEW_TYPE_RECEBIDA = 2;
    private List<String> messageList;

    public RecyclerViewAdapter(List<String> messages) {
        this.messageList = messages;
    }

    @Override
    public int getItemViewType(int position) {
        //tem que arrumar isso daqui ainda, o usuario não pode enviar pelos 2 layouts
        return position % 2 == 0 ? R.layout.chat_one_line_enviamsg : R.layout.chat_one_line_recebemsg;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String message = messageList.get(position);
        ((MessageViewHolder) holder).bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUsrName;
        public TextView textViewUsrMsg;
        public TextView textViewNomeRecebeMsg;
        public TextView textViewMsgRecebe;

        public MessageViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == R.layout.chat_one_line_enviamsg) {
                textViewUsrName = itemView.findViewById(R.id.textViewUsrName);
                textViewUsrMsg = itemView.findViewById(R.id.textViewUsrMsg);
            }
            else if (viewType == R.layout.chat_one_line_recebemsg) {
                textViewNomeRecebeMsg = itemView.findViewById(R.id.textViewNomeRecebeMsg);
                textViewMsgRecebe = itemView.findViewById(R.id.textViewMsgRecebe);
            }
        }

        public void bindMessage(String message) {
            if (textViewUsrMsg != null) {
                textViewUsrMsg.setText(message);
            } else if (textViewMsgRecebe != null) {
                textViewMsgRecebe.setText(message);
            }
        }
    }
}