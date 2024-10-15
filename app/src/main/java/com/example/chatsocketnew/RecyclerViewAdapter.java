package com.example.chatsocketnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder> {
    //lista de msg que vai ser usada no recyclerview
    private List<Message> messageList;

    //construtor que recebe a lista
    public RecyclerViewAdapter(List<Message> messages) {
        this.messageList = messages;
    }

    @Override
    public int getItemViewType(int position) {
        //determina se é uma msg enviada ou recebida
        Message message = messageList.get(position);
        return message.isSentByUser() ? R.layout.chat_one_line_enviamsg : R.layout.chat_one_line_recebemsg;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //infla o layout com base no tipo de msg
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        //liga os dados da msg com o viewholder
        Message message = messageList.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //viewholder para manter as referencias para as views
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsrMsg;
        private TextView textViewUsrName;
        private TextView textViewMsgRecebe;
        private TextView textViewNomeRecebe;

        public MessageViewHolder(View itemView, int viewType) {
            super(itemView);

            //views para msg enviada
            if (viewType == R.layout.chat_one_line_enviamsg) {
                textViewUsrMsg = itemView.findViewById(R.id.textViewUsrMsg);
                textViewUsrName = itemView.findViewById(R.id.textViewUsrName);
                //views para msg recebida
            } else if (viewType == R.layout.chat_one_line_recebemsg) {
                textViewMsgRecebe = itemView.findViewById(R.id.textViewMsgRecebe);
                textViewNomeRecebe = itemView.findViewById(R.id.textViewNomeRecebeMsg);
            }
        }

        //vincula os dados da mensagem as views
        public void bindMessage(Message message) {
            if (message.isSentByUser()) {

                //atualiza o texto das views se foi enviada pelo usr
                if (textViewUsrMsg != null) {
                    textViewUsrMsg.setText(message.getContent());
                }
                if (textViewUsrName != null) {
                    textViewUsrName.setText(message.getUsername());
                }
            } else {

                //atualiza o texto das views se foi recebida
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
