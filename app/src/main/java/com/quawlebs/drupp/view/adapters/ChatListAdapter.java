package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.BaseMessage;
import com.quawlebs.drupp.models.ChatReceivedMessage;
import com.quawlebs.drupp.models.ChatUserMessage;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.ImageUtils;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Timing;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseMessage> mMessageList;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatListAdapter(Context context, List<BaseMessage> messageModels) {
        mContext = context;
        mMessageList = messageModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_chat_box, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_driver_chat_box, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseMessage message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind((ChatUserMessage) message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind((ChatReceivedMessage) message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = mMessageList.get(position);
        if (message instanceof ChatUserMessage) {
            //This is Sent Message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            //This is recevied message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView sentMessage, atTimeMessage;
        ImageView messageImage;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.iv_attachment_image);
            sentMessage = itemView.findViewById(R.id.text_message_body);
            atTimeMessage = itemView.findViewById(R.id.text_message_time);
        }

        void bind(ChatUserMessage message) {
            try {
                if (message.getIsType().equalsIgnoreCase(String.valueOf(AppConstants.IS_IMAGE))) {
                    sentMessage.setVisibility(View.GONE);
                    messageImage.setVisibility(View.VISIBLE);
                    ImageUtils.displayRoundImageFromUrl(mContext, message.getImageMessage(), messageImage);
                } else {
                    sentMessage.setVisibility(View.VISIBLE);
                    messageImage.setVisibility(View.GONE);
                    sentMessage.setText(message.getMessage());
                }


            } catch (Exception e) {

            }
            // Format the stored timestamp into a readable String using method.
            atTimeMessage.setText(Timing.getTimeInString(message.getCreatedAt(), Timing.TimeFormats.HH_12));

            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView receivedMessage, senderName, atTimeMessage;
        ImageView messageImage;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.iv_attachment_image);
            receivedMessage = itemView.findViewById(R.id.tv_received_message_text);
            atTimeMessage = itemView.findViewById(R.id.text_message_time);
            senderName = itemView.findViewById(R.id.text_message_name);
        }

        void bind(ChatReceivedMessage message) {
            try {

                if (message.getIsType().equalsIgnoreCase(String.valueOf(AppConstants.IS_IMAGE))) {
                    receivedMessage.setVisibility(View.GONE);
                    messageImage.setVisibility(View.VISIBLE);
                    ImageUtils.displayRoundImageFromUrl(mContext, message.getImageMessage(), messageImage);
                } else {
                    receivedMessage.setVisibility(View.VISIBLE);
                    messageImage.setVisibility(View.GONE);
                    receivedMessage.setText(message.getMessage());
                }

                senderName.setText(message.getReceiver());

            } catch (Exception e) {

            }


            // Format the stored timestamp into a readable String using method.
            atTimeMessage.setText(Timing.getTimeInString(message.getCreatedAt(), Timing.TimeFormats.HH_12));


            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }


}
