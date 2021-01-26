package com.alex.vr_party_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.chat.Chat;
import com.alex.vr_party_app.chat.ChatAdapter;
import com.alex.vr_party_app.databinding.FragmentChatBinding;
import com.alex.vr_party_app.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;

public class ChatFragment extends Fragment {

    FragmentChatBinding binding;

    private EmojIconActions emojIconActions;

    ChatAdapter mChatAdapter;
    ArrayList<Chat> chat = new ArrayList<>();

    DatabaseReference reference;

    User user;
    String userName;
    String email;
    String userId;
    String userIdOther;
    String imageURL;

    Toolbar toolbar;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        binding.listMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatAdapter = new ChatAdapter(getContext(), chat);
        binding.listMessages.setAdapter(mChatAdapter);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emojIconActions = new EmojIconActions(getContext(), binding.constraintLayout, binding.messageField, binding.emoji);
        emojIconActions.ShowEmojIcon();
        emojIconActions.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.ic_tag_faces);
        emojIconActions.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.d("AlexDebug", "open");
                binding.listMessages.scrollToPosition(chat.size()-1);
            }
            @Override
            public void onKeyboardClose() {
                Log.d("AlexDebug", "close");
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid().toString();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User tempUser = dataSnapshot.getValue(User.class);
                userName = tempUser.getUserName();
                imageURL = tempUser.getImageURL();
                readMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.messageField.getText().toString();
                if (!message.equals("")) {
                    sendMessage(userName, message, userId, imageURL);
                }
                binding.messageField.setText("");
            }
        });
    }




    private void sendMessage(String userName, String message, String userID, String mImageURL) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", userName);
        hashMap.put("message", message);
        hashMap.put("userid", userID);
        hashMap.put("imageURL", mImageURL);
        reference.child("Chats").push().setValue(hashMap);
    }





    private void readMessages() {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mChat.clear();
//                ArrayList<Chat>temp=new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    Log.d("AlexDebug", "getMessage: " + chat.getMessage());
//                    Log.d("AlexDebug", "getUserid: " + chat.getUserid());
//                    Log.d("AlexDebug", "getUsername: " + chat.getUsername());
//                    //if (chat.getUser().equals(userId)) {
//                    temp.add(chat);
//
//                }
//                mChat.addAll(temp);
//                //}
//                mChatAdapter.notifyDataSetChanged();
//                //прокрутка была стремилась к низу
//                list_messages.smoothScrollToPosition(mChat.size());
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        reference.addChildEventListener(new ChildEventListener() {
            //вызывается когда происходит добавление нового сообщения в базу
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat message = dataSnapshot.getValue(Chat.class);
//                Log.d("AlexDebug", "getMessage: " + chat.getMessage());
//                Log.d("AlexDebug", "getUserid: " + chat.getUserid());
//                Log.d("AlexDebug", "getUsername: " + chat.getUsername());
//                Log.d("AlexDebug", "getImageURL: " + chat.getImageURL());
                chat.add(message);

                mChatAdapter.notifyDataSetChanged();
                binding.listMessages.smoothScrollToPosition(chat.size()-1);
                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}