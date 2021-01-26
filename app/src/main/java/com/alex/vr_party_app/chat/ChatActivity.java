package com.alex.vr_party_app.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.support.ScreenDimensions;
import com.alex.vr_party_app.databinding.ActivityChatBinding;
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

public class ChatActivity extends AppCompatActivity {

    private EmojIconActions emojIconActions;

    ChatAdapter mChatAdapter;
    ArrayList<Chat> mChat = new ArrayList<>();

    DatabaseReference reference;

    User user;
    String userName;
    String email;
    String userId;
    String userIdOther;
    String imageURL;

    Toolbar toolbar;

    ScreenDimensions screenDimension;

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        screenDimension = new ScreenDimensions(this);
        //hideToolBr();

//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getText(R.string.chat));
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emojIconActions = new EmojIconActions(getApplicationContext(), binding.constraintLayout, binding.messageField, binding.emoji);
        emojIconActions.ShowEmojIcon();
        emojIconActions.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.ic_tag_faces);
        emojIconActions.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.d("AlexDebug", "open");
                binding.listMessages.scrollToPosition(mChat.size()-1);

            }

            @Override
            public void onKeyboardClose() {
                Log.d("AlexDebug", "close");
            }
        });

        //binding.listMessages.getLayoutParams().height = (int) (((screenDimension.getHeightDP() - 110)) * screenDimension.getScreenDensity() + 0.5f);

        //list_messages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        binding.listMessages.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(this, mChat);
        binding.listMessages.setAdapter(mChatAdapter);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void hideToolBr() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            //Log.i(Constants.TAG_DEF, "Turning immersive mode mode off. ");
        } else {
            //Log.i(Constants.TAG_DEF, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)

    }


    private void sendMessage(String userName, String message, String userID, String mImageURL) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", userName);
        hashMap.put("message", message);
        hashMap.put("userid", userID);
        hashMap.put("imageURL", mImageURL);
        reference.child("ChatTest").push().setValue(hashMap);
    }

    private void readMessages() {
        reference = FirebaseDatabase.getInstance().getReference("ChatTest");
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
                Chat chat = dataSnapshot.getValue(Chat.class);
//                Log.d("AlexDebug", "getMessage: " + chat.getMessage());
//                Log.d("AlexDebug", "getUserid: " + chat.getUserid());
//                Log.d("AlexDebug", "getUsername: " + chat.getUsername());
//                Log.d("AlexDebug", "getImageURL: " + chat.getImageURL());
                mChat.add(chat);

                mChatAdapter.notifyDataSetChanged();
                binding.listMessages.smoothScrollToPosition(mChat.size());
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
