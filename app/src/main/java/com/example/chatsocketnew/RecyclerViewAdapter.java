package com.example.chatsocketnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> messageList;

    public RecyclerViewAdapter(List<String> messages) {
        this.messageList = messages;
    }

    @Override
    public int getItemViewType(int position) {
        //posição par mensagem enviada e ímpar seja recebida
        return position % 2 == 0 ? R.layout.chat_one_line_enviamsg : R.layout.chat_one_line_recebemsg;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String message = messageList.get(position);
        ((MessageViewHolder) holder).textViewMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            //tem que ser finalizado
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}