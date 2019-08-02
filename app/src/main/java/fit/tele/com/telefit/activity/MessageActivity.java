package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ChatFirebaseAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityMessageBinding;
import fit.tele.com.telefit.interfaces.ClickListenerChatFirebase;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.chat.ChatModel;
import fit.tele.com.telefit.modelBean.chat.UserModel;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.FileUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MessageActivity extends BaseActivity implements ClickListenerChatFirebase{

    private String CHAT_REFERENCE = "";
    private String CHAT_REFERENCE1 = "";
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final int FILE_PICKER_REQUEST = 4;

    private UserModel user;
    private UserModel userModel;
    private UserModel userModel1;
    private EmojiPopup emojiPopup;
    private ActivityMessageBinding binding;
    private DatabaseReference mFirebaseDatabaseReference;

    private Uri outputFileUri;
    private LinearLayoutManager mLinearLayoutManager;

    private long DOWNLOAD_ID=-1;
    String friend_id = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message;
    }

    @Override
    public void init() {
        if(getIntent() != null && getIntent().hasExtra("user_model"))
            user = getIntent().getParcelableExtra("user_model");

        binding = (ActivityMessageBinding) getBindingObj();
        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setStackFromEnd(true);

        if (user!=null){
            binding.txtHeaderName.setText(user.getName());
        }

      //  setUpEmojiPopup();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.imgEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });
        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageFirebase();
            }
        });


        LoginBean saveLogiBean = null;
        String photoUrl = "";
        String useName = "";
        String id = "";


        if (preferences.getUserDataPref() != null)
            saveLogiBean = preferences.getUserDataPref();
        if (saveLogiBean != null && saveLogiBean.getProfilePic() != null && !TextUtils.isEmpty(saveLogiBean.getProfilePic()))
            photoUrl = saveLogiBean.getProfilePic();
        if (saveLogiBean != null && saveLogiBean.getName() != null && !TextUtils.isEmpty(saveLogiBean.getName()))
            useName = saveLogiBean.getName();
        if (saveLogiBean != null && saveLogiBean.getId() != null) {
            id = String.valueOf(saveLogiBean.getId());
        }

        if(user != null) {
            CHAT_REFERENCE = id;
            if(user.getFriend_id().equalsIgnoreCase(id)){
                friend_id = user.getUser_id();
                CHAT_REFERENCE1 = user.getUser_id();
            //    userModel1 = new UserModel(user.getName(), user.getPhoto_profile(), user.ge(),friend_id);
            }else {
                friend_id = user.getFriend_id();
                CHAT_REFERENCE1 = friend_id;
            }
        }

        Log.w("firebasemessage","CHAT_REFERENCE: "+CHAT_REFERENCE);
        Log.w("firebasemessage","CHAT_REFERENCE1: "+CHAT_REFERENCE1);
        Log.w("firebasemessage","Friend_id: "+friend_id);

        userModel = new UserModel(useName, photoUrl, id,friend_id);



        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyBHW9m9Xbz-fqY3uMBLpmzgu4ymdR89nk8")
                .setApplicationId("1:323428350483:android:13d70f9ddb40214a")
                .setDatabaseUrl("https://telfittest.firebaseio.com/")
                .setStorageBucket("gs://telfittest.appspot.com")
                .build();
        try {
            FirebaseApp secondApp = FirebaseApp.initializeApp(this, options, "second app");
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance(secondApp).getReference();
        }catch (IllegalStateException e){
            e.printStackTrace();
            try {
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance(FirebaseApp.getInstance("second app")).getReference();
            } catch (IllegalStateException ex){
                ex.printStackTrace();
            }
        }


//        if(mFirebaseDatabaseReference != null && user != null && userModel != null) {
//            Query query = mFirebaseDatabaseReference.child(userModel.getUser_id()).child("chat_list").orderByChild("user_id").equalTo(user.getUser_id());
//            if(query != null)
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (mFirebaseDatabaseReference != null && user != null && userModel != null && dataSnapshot!= null && !dataSnapshot.exists())
//                            Log.w("GoIn","set user data");
//                            mFirebaseDatabaseReference.child(userModel.getUser_id()).child("chat_list").push().setValue(user);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            query = mFirebaseDatabaseReference.child(user.getUser_id()).child("chat_list").orderByChild("user_id").equalTo(user.getUser_id());
//            if(query != null)
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (mFirebaseDatabaseReference != null && user != null && userModel != null && dataSnapshot!= null && !dataSnapshot.exists())
//                            Log.w("GoIn","set userModel data");
//                            mFirebaseDatabaseReference.child(user.getUser_id()).child("chat_list").push().setValue(userModel);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//        }

        if (mFirebaseDatabaseReference != null && userModel != null && !TextUtils.isEmpty(CHAT_REFERENCE)) {
            Query query = mFirebaseDatabaseReference.child(CHAT_REFERENCE).child("chatModel").orderByChild("user_id").equalTo(CHAT_REFERENCE1);
            if(query != null)
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mFirebaseDatabaseReference != null && user != null && userModel != null && dataSnapshot!= null && !dataSnapshot.exists()) {
                            Log.w("GoIn", "set user data");
                              mFirebaseDatabaseReference.child(CHAT_REFERENCE).child("chatModel").push().setValue(user);
                        }
//                        if (dataSnapshot.exists()){
//                            mFirebaseDatabaseReference.child(CHAT_REFERENCE).child("chatModel").updateChildren().setValue(user);
//                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        }
        if (mFirebaseDatabaseReference != null && userModel != null && !TextUtils.isEmpty(CHAT_REFERENCE)) {
            Query query = mFirebaseDatabaseReference.child(CHAT_REFERENCE1).child("chatModel").orderByChild("user_id").equalTo(CHAT_REFERENCE);
            if(query != null)
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mFirebaseDatabaseReference != null && user != null && userModel != null && dataSnapshot!= null && !dataSnapshot.exists()) {
                            Log.w("GoIn", "set user data");
                            mFirebaseDatabaseReference.child(CHAT_REFERENCE1).child("chatModel").push().setValue(userModel);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        }
//        if (mFirebaseDatabaseReference != null && userModel != null && !TextUtils.isEmpty(CHAT_REFERENCE1)) {
//            mFirebaseDatabaseReference.child(CHAT_REFERENCE).child("chatModel").push().setValue(user);
//        }


        lerMessagensFirebase();
    }

    @Override
    public void onBackPressed() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }
        super.onStop();
    }

//    private void setUpEmojiPopup() {
//        emojiPopup = EmojiPopup.Builder.fromRootView(binding.getRoot())
//                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
//                    @Override
//                    public void onEmojiBackspaceClick(final View v) {
//                    }
//                })
//                .setOnEmojiClickListener(new OnEmojiClickListener() {
//                    @Override
//                    public void onEmojiClick(@NonNull final EmojiImageView imageView, @NonNull final Emoji emoji) {
//                    }
//                })
//                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
//                    @Override
//                    public void onEmojiPopupShown() {
//                       // binding.imgEmoji.setImageResource(R.drawable.ic_keyboard);
//                    }
//                })
//                .setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
//                    @Override
//                    public void onKeyboardOpen(@Px final int keyBoardHeight) {
//                    }
//                })
//                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
//                    @Override
//                    public void onEmojiPopupDismiss() {
//                       // binding.imgEmoji.setImageResource(R.drawable.happiness);
//                    }
//                })
//                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
//                    @Override
//                    public void onKeyboardClose() {
//                    }
//                })
//                .build(binding.edtChatBottomMessage);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null && FileUtils.getFile(context, data.getData()).length() != 0) {
                    File file = new File((FileUtils.getFile(context, data.getData()).getPath()));
                    if (file != null && file.exists()) {
                        // callUploadImageApi(file,true);
                    }
                    else
                        CommonUtils.toast(context, "Fail image get");
                } else
                    CommonUtils.toast(context, "Fail image get");
            }
        } else if (requestCode == IMAGE_CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                File file = new File((FileUtils.getFile(context, outputFileUri).getPath()));
                if (file != null && file.exists()) {
                    //callUploadImageApi(file,true);
                } else
                    CommonUtils.toast(context, "Fail image get");
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //  Place place = PlacePicker.getPlace(context, data);
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null && place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                   // MapModel mapModel = new MapModel(latLng.latitude + "", latLng.longitude + "");
                  //  ChatModel chatModel = new ChatModel(userModel, Calendar.getInstance().getTime().getTime() + "", mapModel);
//                    if(mFirebaseDatabaseReference != null && !TextUtils.isEmpty(CHAT_REFERENCE))
//                        mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(chatModel);
                } else
                    CommonUtils.toast(context, "Fail image get");
            }
        } else if(requestCode == FILE_PICKER_REQUEST) {
            if (data != null && data.getData() != null && FileUtils.getFile(context, data.getData()).length() != 0) {
                File finalFile = new File((FileUtils.getFile(context, data.getData()).getPath()));
                if (finalFile != null && finalFile.exists()) {
                  //  callUploadImageApi(finalFile, false);
                }
                else
                    CommonUtils.toast(context, "Fail image get");
            } else
                CommonUtils.toast(context, context.getString(R.string.toast_image_empty));
        }

    }

    private void sendMessageFirebase(){

        Log.w("firebasemessage","Go in sendMessageFirebase: ");
        final String text = binding.edtChatBottomMessage.getText().toString().trim();

        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text);
        Matcher m1 = Pattern.compile("[1-9][0-9]{9,10}").matcher(text);

        if (m.find())
        {
            CommonUtils.toast(context,"Not allowed to enter Email ID or Phone number.");
            return;
        }
        else
        {
            if (m1.find())
            {
                CommonUtils.toast(context,"Not allowed to enter Email ID or Phone number.");
                return;
            }
            else
            {
                if (mFirebaseDatabaseReference != null && userModel != null && !TextUtils.isEmpty(CHAT_REFERENCE) && text.length() > 0) {
                    ChatModel model = new ChatModel(userModel,text, Calendar.getInstance().getTime().getTime()+"");
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE1).child(CHAT_REFERENCE).push().setValue(model);
                //    mFirebaseDatabaseReference.child(CHAT_REFERENCE1).child("chatModel").push().setValue(userModel);
                    sendNotification(userModel.getFriend_id());
                    binding.edtChatBottomMessage.setText("");
                }
                if (mFirebaseDatabaseReference != null && userModel != null && !TextUtils.isEmpty(CHAT_REFERENCE1) && text.length() > 0) {
                    ChatModel model = new ChatModel(userModel,text, Calendar.getInstance().getTime().getTime()+"");
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(CHAT_REFERENCE1).push().setValue(model);
                 //   mFirebaseDatabaseReference.child(CHAT_REFERENCE).child("chatModel").push().setValue(userModel);
                    sendNotification(userModel.getFriend_id());
                    binding.edtChatBottomMessage.setText("");
                }
            }
        }





    }

    private void lerMessagensFirebase(){
        Log.w("firebasemessage","Go in lerMessagensFirebase()");
        if(mFirebaseDatabaseReference != null && userModel != null && !TextUtils.isEmpty(CHAT_REFERENCE)) {
            final ChatFirebaseAdapter firebaseAdapter = new ChatFirebaseAdapter(mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(CHAT_REFERENCE1), userModel.getName(), this);

            Log.w("firebasemessage","ChatPReference: "+mFirebaseDatabaseReference.child(CHAT_REFERENCE));
            Log.w("firebasemessage","User messagegetname: "+userModel.getName());

            firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    Log.w("firebasemessage","onItemRangeInserted");
                    if(firebaseAdapter != null && mLinearLayoutManager != null) {
                        int friendlyMessageCount = firebaseAdapter.getItemCount();
                        int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                        Log.w("firebasemessage","onItemRangeInserted "+friendlyMessageCount+" "+lastVisiblePosition);

                        if (lastVisiblePosition == -1 ||
                                (positionStart >= (friendlyMessageCount - 1) &&
                                        lastVisiblePosition == (positionStart - 1))) {
                            binding.recyclerView.scrollToPosition(positionStart);
                        }
                    }if(firebaseAdapter==null){

                    }
                }
            });
            binding.recyclerView.setLayoutManager(mLinearLayoutManager);
            binding.recyclerView.setAdapter(firebaseAdapter);
        }
    }

    @Override
    public void clickImageChat(View view, int position, String nameUser, String urlPhotoUser, String urlPhotoClick) {

    }

    @Override
    public void clickFileChat(View view, int position, String nameUser, String urlFileUser, String urlFileClick) {

    }

    @Override
    public void clickImageMapChat(View view, int position, String latitude, String longitude) {

    }


//    public static void sendNotificationToUser(String user, final String message) {
////        Firebase ref = new Firebase(FIREBASE_URL);
////        final Firebase notifications = ref.child("notificationRequests");
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setApiKey("AIzaSyAi45QlOunmlieuGz-0KzNYZN32rwH35lY")
//                .setApplicationId("1:334843586762:android:01f75a701adf77bd")
//                .setDatabaseUrl("https://gatecrasher-partner.firebaseio.com")
//                .setStorageBucket("gs://gatecrasher-partner.appspot.com")
//                .build();
//
//        Map notification = new HashMap<>();
//        notification.put("username", user);
//        notification.put("message", message);
//
////        mFirebaseDatabaseReference.push().setValue(notification);
//    }


    private void sendNotification(String friednId) {
        if (CommonUtils.isInternetOn(context)) {

            Map<String, String> map = new HashMap<>();
            map.put("friend_id",friednId);
            Observable<ModelBean<UserModel>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).sendNotification(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<UserModel>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanDetails"," "+e);

                        }
                        @Override
                        public void onNext(ModelBean<UserModel> loginBean) {

                            if(loginBean.getStatus()==1){
                                //   binding.progress.setVisibility(View.GONE);

                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

}
