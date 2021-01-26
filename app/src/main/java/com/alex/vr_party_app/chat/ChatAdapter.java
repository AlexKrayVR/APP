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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private static final int MSG_TYPE_LEFT_R = 2;
    private static final int MSG_TYPE_RIGHT_R = 3;
    private ArrayList<Chat> mChat = new ArrayList<>();
    LayoutInflater inflater;
    private Context context;
    private String mImageURL;
    FirebaseUser mUser;

    private static boolean left = false;
    private static boolean right = false;
    private static String previous = "";

    public ChatAdapter(Context context, ArrayList<Chat> mChat) {
        this.mChat = mChat;
        this.context = context;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("AlexDebug", "constructor");

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AlexDebug", "viewType: " + viewType);

        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.ViewHolder(v);
        } else  {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.ViewHolder(v);
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


        Chat chat = mChat.get(position);
        Log.d("AlexDebug", "message: " + chat.getMessage());


        holder.show_message.setText(chat.getMessage());
        holder.user_name.setText(chat.getUsername());
        //Log.d("AlexDebug", "mImageURL: " + mImageURL);
        //holder.show_message.setText(chat.getMessage());
        mImageURL = chat.getImageURL();
        //Log.d("AlexDebug", "Bind mImageURL: " + mImageURL);
        if (mImageURL.equals("default")) {
            holder.profile_image.setImageResource(R.drawable.candy);
        } else {
            Picasso.get().load(mImageURL).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message, user_name;
        public CircleImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profileImage);
            user_name = itemView.findViewById(R.id.user_name);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d("AlexDebug", "getUid: " + mUser.getUid().toString());
        if (mChat.get(position).getUserid().equals(mUser.getUid().toString())) {
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