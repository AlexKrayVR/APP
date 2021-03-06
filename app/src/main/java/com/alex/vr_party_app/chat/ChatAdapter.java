package com.alex.vr_party_app.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.vr_party_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private static final int MSG_TYPE_LEFT_R = 2;
    private static final int MSG_TYPE_RIGHT_R = 3;
    private ArrayList<Chat> chat;
    LayoutInflater inflater;
    private Context context;
    private String imageURL;
    FirebaseUser user;
    SimpleDateFormat timeFormatDisplay = new SimpleDateFormat("HH:mm MMMM dd ", Locale.ENGLISH);
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    //private static boolean left = false;
    //private static boolean right = false;
    //private static String previous = "";

    public ChatAdapter(Context context, ArrayList<Chat> chat) {
        this.chat = chat;
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AlexDebug", "viewType: " + viewType);
        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(v);
        }
//         else if (viewType == MSG_TYPE_RIGHT_R) {
//            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right_repeat, parent, false);
//            return new ChatAdapter.ViewHolder(v);
//        } else {
//            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left_repeat, parent, false);
//            return new ChatAdapter.ViewHolder(v);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = this.chat.get(position);
        Log.d("AlexDebug", "chat: " + chat.toString());
        holder.userMessage.setText(chat.getMessage());
        holder.userName.setText(chat.getUserName());

        String date = chat.getDate();
        Calendar calendar = GregorianCalendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(timeFormat.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int differentTIME = TimeZone.getDefault().getRawOffset() / 1000; //in seconds
        calendar.add(Calendar.SECOND, differentTIME);
        holder.date.setText(timeFormatDisplay.format(calendar.getTime()));

        imageURL = chat.getImageURL();
        if (imageURL.equals("default")) {
            holder.userImage.setImageResource(R.drawable.candy);
        } else {
            Picasso.get().load(imageURL).resize(200, 0).centerCrop().into(holder.userImage);
        }
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userMessage, userName, date;
        public CircleImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessage = itemView.findViewById(R.id.userMessage);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            date = itemView.findViewById(R.id.date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d("AlexDebug", "getUid: " + mUser.getUid().toString());
        if (chat.get(position).getUserID().equals(user.getUid())) {
//            left = false;
//            previous = "";
//            if (right) {
//                return MSG_TYPE_RIGHT_R;
//            } else {
//                right = true;
            return MSG_TYPE_RIGHT;
            //}
        } else {
//            right = false;
//            if (previous.equals(mChat.get(position).getUserid().toString())) {
//                left = false;
//                return MSG_TYPE_LEFT_R;
//            }
//            previous = mChat.get(position).getUserid().toString();
//            if (left) {
//                return MSG_TYPE_LEFT_R;
//            } else {
//                left = true;
            return MSG_TYPE_LEFT;
            //}
        }
        //return super.getItemViewType(position);
    }
}