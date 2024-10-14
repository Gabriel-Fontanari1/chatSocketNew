package com.example.chatsocketnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder> {
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
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsrMsg; // mensagens enviadas
        private TextView textViewUsrName; // usuário de mensagens enviadas
        private TextView textViewMsgRecebe; // mensagens recebidas
        private TextView textViewNomeRecebe; // nome usuário de mensagens recebidas

        public MessageViewHolder(View itemView, int viewType) {
            super(itemView);

            // inicia as views baseado no tipo de layout
            if (viewType == R.layout.chat_one_line_enviamsg) {
                textViewUsrMsg = itemView.findViewById(R.id.textViewUsrMsg);
                textViewUsrName = itemView.findViewById(R.id.textViewUsrName);
            } else if (viewType == R.layout.chat_one_line_recebemsg) {
                textViewMsgRecebe = itemView.findViewById(R.id.textViewMsgRecebe);
                textViewNomeRecebe = itemView.findViewById(R.id.textViewNomeRecebeMsg);
            }
        }

        public void bindMessage(Message message) {
            if (message.isSentByUser()) {
                // Configura as mensagens enviadas
                if (textViewUsrMsg != null) {
                    textViewUsrMsg.setText(message.getContent());
                }
                if (textViewUsrName != null) {
                    textViewUsrName.setText(message.getUsername());
                }
            } else {
                // Configura as mensagens recebidas
                if (textViewMsgRecebe != null) {
                    textViewMsgRecebe.setText(message.getContent());
                }
                if (textViewNomeRecebe != null) {
                    textViewNomeRecebe.setText(message.getUsername());
                }
            }
        }
    }
}
