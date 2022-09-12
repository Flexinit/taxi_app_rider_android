package com.quawlebs.drupp.view.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.models.BaseMessage;
import com.quawlebs.drupp.models.ChatReceivedMessage;
import com.quawlebs.drupp.models.ChatUserMessage;
import com.quawlebs.drupp.view.adapters.ChatListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends MainBaseActivity {

    //---------------------Views------------------
    private RecyclerView chatRecyclerView;
    private ImageButton sendButton;
    private EditText mEditMessage;
    //---------------------List&Adapter-----------
    private ChatListAdapter chatListAdapter;
    private List<BaseMessage> messageList;
    private String mUsername;
    //---------------------Globals----------------
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mStorage;
    private StorageReference mChatPhotosStorageRef;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mEditMessage = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);
        chatRecyclerView = findViewById(R.id.reyclerview_message_list);

        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES);
        mChatPhotosStorageRef = mStorage.getReference().child(AppConstants.PHOTOS);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatListAdapter = new ChatListAdapter(this, messageList);
        chatRecyclerView.setAdapter(chatListAdapter);


        findViewById(R.id.im_back).setOnClickListener(v -> onBackPressed());

        mUsername = AppConstants.ANONYMOUS;

        //Listeners
        sendButton.setOnClickListener(v -> {

            ChatUserMessage message = new ChatUserMessage(mEditMessage.getText().toString(), mUsername);

            mMessagesReference.push().setValue(message);

            mEditMessage.setText("");
        });

        mAuthStateListener = firebaseAuth -> {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                onSignedInInitialize(user.getDisplayName());
            } else {
                onSignedOutCleanup();
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                ))
                                .build(),
                        AppConstants.RC_SIGN_IN);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        detachDatabaseReadListener();
        messageList.clear();
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = AppConstants.ANONYMOUS;
        messageList.clear();
        chatListAdapter.notifyDataSetChanged();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Received Messages
                    ChatReceivedMessage message = dataSnapshot.getValue(ChatReceivedMessage.class);
                    messageList.add(message);
                    chatListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessagesReference.addChildEventListener(mChildEventListener);
        }
    }


}
