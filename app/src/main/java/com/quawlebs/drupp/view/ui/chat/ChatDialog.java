package com.quawlebs.drupp.view.ui.chat;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.models.Author;
import com.quawlebs.drupp.models.Chat;
import com.quawlebs.drupp.models.ChatReceivedMessage;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.network.VolleySingleton;
import com.quawlebs.drupp.view.ui.MainActivity;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ChatDialog extends BottomSheetDialogFragment implements ChatView<ChatViewState> {

    @BindView(R.id.message_list)
    MessagesList messagesListView;
    @BindView(R.id.input_message)
    MessageInput sendInput;
    @BindView(R.id.tv_toolbar_title)
    TextView toolbarTitle;


    private ImageLoader imageLoader;
    private MessagesListAdapter<Chat> messagesListAdapter;
    private Unbinder unbinder;
    private UserInfo riderInfo;
    private RideInfo driverInfo;
    private PublishSubject<JSONObject> sendSubject = PublishSubject.create();
    private ChatViewModel chatViewModel;
    //FireBase
    private ChildEventListener mChildEventListener;
    private ValueEventListener mChildSingleValueEventListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private FirebaseStorage mStorage;
    private StorageReference mChatPhotosStorageRef;
    private String chatId;
    private Chat chat;
    private String TOPIC;
    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private boolean initChatHistory = true;

    public static ChatDialog newInstance(RideInfo driverInfo) {

        Bundle args = new Bundle();
        args.putParcelable(AppConstants.K_RIDE_INFO, driverInfo);
        ChatDialog fragment = new ChatDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        sendSubject.onComplete();
        mMessagesReference.removeEventListener(mChildSingleValueEventListener);
        mMessagesReference.removeEventListener(mChildEventListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        riderInfo = SessionManager.getInstance().loadUser(getContext()).getUserInfo();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;

    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        driverInfo = getArguments().getParcelable(AppConstants.K_RIDE_INFO);
        chatId = riderInfo.getId() + "_" + driverInfo.getDriverId();

        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(chatId);
        mChatPhotosStorageRef = mStorage.getReference().child(AppConstants.PHOTOS).child(chatId);


        imageLoader = (imageView, url, payload) -> Glide.with(this).load(url).apply(new RequestOptions()
                .error(R.drawable.no_image_available)
                .centerCrop().placeholder(R.drawable.no_image_available)).into(imageView);
        messagesListAdapter = new MessagesListAdapter<>(riderInfo.getId().toString(), imageLoader);
        messagesListView.setAdapter(messagesListAdapter);


        toolbarTitle.setText(driverInfo.getDriverName());
        sendInput.setInputListener(input -> {
            sendChat(input.toString());
            return !input.toString().isEmpty();
        });
        attachListeners();
    }

    private void attachListeners() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String s) {
                    //Create Chat
                    if (!initChatHistory) {
                        try {
                            DataSnapshot userSnapshot = dataSnapshot.child("user");
                            Author author = new Author(userSnapshot.child("id").getValue().toString(), userSnapshot.child("name").getValue().toString(), "");
                            Chat chat = new Chat(riderInfo.getId().toString(), dataSnapshot.child("message").getValue().toString(), author);
                            if (dataSnapshot.child("time").getValue() != null) {
                                chat.setCreatedAt(Objects.requireNonNull(dataSnapshot.child("time").getValue()).toString());
                            }

                            messagesListAdapter.addToStart(chat, true);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

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

        }

        if (mChildSingleValueEventListener == null) {
            mChildSingleValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (initChatHistory) {
                        try {
                            List<Chat> chatList = new ArrayList<>();
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                //Create Chat
                                DataSnapshot userSnapshot = childSnapshot.child("user");
                                Author author = new Author(userSnapshot.child("id").getValue().toString(), userSnapshot.child("name").getValue().toString(), "");
                                Chat chat = new Chat(riderInfo.getId().toString(), childSnapshot.child("message").getValue().toString(), author);
                                if (childSnapshot.child("time").getValue() != null) {
                                    chat.setCreatedAt(Objects.requireNonNull(childSnapshot.child("time").getValue()).toString());
                                }
                                chatList.add(chat);

                            }
                            messagesListAdapter.addToEnd(chatList, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    initChatHistory = false;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }
        mMessagesReference.addChildEventListener(mChildEventListener);
        mMessagesReference.addListenerForSingleValueEvent(mChildSingleValueEventListener);
    }


    private void sendChat(String message) {
        try {
            Author author = new Author(riderInfo.getId().toString(), riderInfo.getFirstName(), "");

            chat = new Chat(message, author);
            chat.setSender(riderInfo.getFirstName());
            chat.setSenderId(riderInfo.getId().toString());
            chat.setCreatedAt(Timing.getTimeInString(Timing.getCurrentTimeEpoch(), Timing.TimeFormats.YYYY_MM_DD_HH_MM_S));
            chat.setTime(Timing.getTimeInString(Timing.getCurrentTimeEpoch(), Timing.TimeFormats.YYYY_MM_DD_HH_MM_S));
            if (riderInfo != null) {
                chat.setReceiverId(driverInfo.getDriverId().toString());
            }

            String key = mMessagesReference.push().getKey();
            chat.setMessageId(key);
            mMessagesReference.child(key).setValue(chat);

            TOPIC = AppConstants.K_TOPIC + driverInfo.getDriverId() + driverInfo.getRideId();
            NOTIFICATION_TITLE = riderInfo.getFirstName();
            NOTIFICATION_MESSAGE = chat.getMessage();

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();

            notifcationBody.put(AppConstants.K_TITLE, NOTIFICATION_TITLE);
            notifcationBody.put(AppConstants.K_MESSAGE_NOTIFY, NOTIFICATION_MESSAGE);
            notifcationBody.put(AppConstants.K_BODY, NOTIFICATION_MESSAGE);
            notifcationBody.put(AppConstants.K_TYPE, 30);

            notification.put(AppConstants.K_TO, TOPIC);
            notification.put(AppConstants.K_DATA, notifcationBody);

            //  sendSubject.onNext(notification);
            sendNotification(notification);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "ERROR");
        }

    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConstants.FCM_BASE_URL, notification,
                response -> {
                    //  messagesListAdapter.addToStart(chat, true);
                },
                error -> {
                    if(error.getMessage()!=null){

                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("notification error", error.getMessage());
                    }


                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=" + AppConstants.SERVER_KEY);
                params.put("Content-Type", AppConstants.JSON);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public Observable<JSONObject> sendIntent() {
        return sendSubject;
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        dismiss();
    }

    @Override
    public void bindViewModel() {
        chatViewModel = new ChatViewModel(new ChatInteractor());
        chatViewModel.bind(this);
    }

    @Override
    public void render(ChatViewState viewState) {
        if (viewState.progress) {

        } else if (!viewState.error.isEmpty()) {
            Toast.makeText(getContext(), viewState.error, Toast.LENGTH_SHORT).show();
        } else {

            if (viewState.sent) {
                //Single Message Sent
                messagesListAdapter.addToStart(chat, true);
            }
        }
    }
}
