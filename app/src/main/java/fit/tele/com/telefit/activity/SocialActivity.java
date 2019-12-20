package fit.tele.com.telefit.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ActivityAdapter;
import fit.tele.com.telefit.adapter.AllFriendsAdapter;
import fit.tele.com.telefit.adapter.FriendRequestAdapter;
import fit.tele.com.telefit.adapter.MessageFirebaseAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySocialBinding;
import fit.tele.com.telefit.modelBean.CreatePostBean;
import fit.tele.com.telefit.modelBean.CustomerDetailBean;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.chat.UserModel;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.Preferences;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocialActivity extends BaseActivity implements View.OnClickListener{

    ActivitySocialBinding binding;
    private int strSelectedTab = 1;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_req_customers;
    RecyclerView rv_customers,rv_activities,rv_messages;
    LinearLayout ll_create_post;

    FriendRequestAdapter friendRequestAdapter;
    AllFriendsAdapter allFriendsAdapter;
    ActivityAdapter activityAdapter;


    Preferences preferences;
    private DatabaseReference mFirebaseDatabaseReference;
    private String CHAT_REFERENCE = "";
    LinearLayoutManager mLinearLayoutManager;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_social;
    }

    @Override
    public void init() {
        binding = (ActivitySocialBinding)getBindingObj();
        preferences = new Preferences(this);

        if (getIntent()!=null){
            strSelectedTab =  getIntent().getIntExtra("tabStatus",1);
        }

        setListner();
    }

    public void setListner(){
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        ll_create_post = findViewById(R.id.ll_create_post);

        rv_activities = (RecyclerView)findViewById(R.id.rv_activities);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_activities.setLayoutManager(linearLayoutManager);

        rv_messages = (RecyclerView)findViewById(R.id.rv_messages);

        if(strSelectedTab==1){
            setActivity();
        } if(strSelectedTab==2){
            setMessage();
        } if(strSelectedTab==3){
            setFriends();
        } if(strSelectedTab==4){
            setRequests();
        }

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        binding.llActivityTab.setOnClickListener(this);
        binding.llMessagesTab.setOnClickListener(this);
        binding.llFriendsTab.setOnClickListener(this);
        binding.llRequestsTab.setOnClickListener(this);
        binding.txtNew.setOnClickListener(this);
        ll_create_post.setOnClickListener(this);
        binding.txtSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_nutrition:
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_profile:
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_goals:
                intent = new Intent(context, GoalsActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_activity_tab:
                setActivity();
                break;

            case R.id.ll_messages_tab:
                setMessage();
                break;

            case R.id.ll_friends_tab:
                setFriends();

                break;

            case R.id.ll_requests_tab:
                setRequests();

                break;

            case R.id.txt_new:
                Intent in = new Intent(SocialActivity.this,FindFriendActivity.class);
                startActivity(in);
                break;

            case R.id.ll_create_post:
                Intent in1 = new Intent(SocialActivity.this,CreatePost.class);
                startActivityForResult(in1, 1001);
            //    startActivity(in1);
                break;

            case R.id.txt_setting:
                Intent in2 = new Intent(SocialActivity.this,SocialSetting.class);
                startActivity(in2);
                break;
        }
    }

    private void getAllRequests() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            Observable<ModelBean<ArrayList<CustomerDetailBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getAllRequests(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CustomerDetailBean>>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanDetails"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onNext(ModelBean<ArrayList<CustomerDetailBean>> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if(loginBean.getStatus()==1){
                             //   binding.progress.setVisibility(View.GONE);
                                if(loginBean.getResult()!=null)
                                    friendRequestAdapter.addAllList(loginBean.getResult());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void getAllFriends() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            Observable<ModelBean<ArrayList<CustomerDetailBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getAllFriends(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CustomerDetailBean>>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanDetails"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onNext(ModelBean<ArrayList<CustomerDetailBean>> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if(loginBean.getStatus()==1){
                             //   binding.progress.setVisibility(View.GONE);
                                if(loginBean.getResult()!=null)
                                    allFriendsAdapter.addAllList(loginBean.getResult());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void getAllActivities() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            Observable<ModelBean<ArrayList<CreatePostBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getAllActivities(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CreatePostBean>>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanDetails"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onNext(ModelBean<ArrayList<CreatePostBean>> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if(loginBean.getStatus()==1){
                             //   binding.progress.setVisibility(View.GONE);
                                if(loginBean.getResult()!=null) {
                                    activityAdapter.clearAll();
                                    activityAdapter.addAllList(loginBean.getResult());
                                }
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void acceptRequest(String friendId,String is_accept) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("friend_id",friendId);
            map.put("is_accept",is_accept);

            Observable<ModelBean<ArrayList<CustomerDetailBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).acceptRequest(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CustomerDetailBean>>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanDetails"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onNext(ModelBean<ArrayList<CustomerDetailBean>> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            CommonUtils.toast(context, loginBean.getMessage());
                            if(loginBean.getStatus()==1){
                               // binding.progress.setVisibility(View.GONE);
                                if(is_accept.equalsIgnoreCase("3")) {
                                   allFriendsAdapter.clearAll();
                                   if(loginBean.getResult()!=null)
                                    allFriendsAdapter.addAllList(loginBean.getResult());
                                }else {
                                    friendRequestAdapter.clearAll();
                                    if(loginBean.getResult()!=null)
                                        friendRequestAdapter.addAllList(loginBean.getResult());
                                }
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void deletePost(String post_id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("post_id",post_id);

            Observable<ModelBean<ArrayList<CreatePostBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).deletePost(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CreatePostBean>>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("deletePost"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onNext(ModelBean<ArrayList<CreatePostBean>> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            CommonUtils.toast(context, loginBean.getMessage());
                            if(loginBean.getStatus()==1){
                                activityAdapter.clearAll();
                                getAllActivities();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    public void showComfirmDialog(String friendId,String message,String is_accept){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle(message);
            // Add the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    acceptRequest(friendId,is_accept);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK){
            if(strSelectedTab==1) {
                getAllActivities();
            }
        }
    }

    public void getMesagesDetail(){

        LoginBean saveLogiBean = null;
        Preferences preferences = new Preferences(context);
        if (preferences.getUserDataPref() != null)
            saveLogiBean = preferences.getUserDataPref();
        if (saveLogiBean != null && saveLogiBean.getId() != null)
            CHAT_REFERENCE = String.valueOf(saveLogiBean.getId()) + "/chatModel";

        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setStackFromEnd(false);

        FirebaseOptions options = new FirebaseOptions.Builder()
         .setApiKey("AIzaSyBHW9m9Xbz-fqY3uMBLpmzgu4ymdR89nk8")
                .setApplicationId("1:323428350483:android:13d70f9ddb40214a")
                .setDatabaseUrl("https://telfittest.firebaseio.com/")
                .setStorageBucket("gs://telfittest.appspot.com")
                .build();
        try {
            FirebaseApp secondApp = FirebaseApp.initializeApp(context, options, "second app");
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance(secondApp).getReference();
        }catch (IllegalStateException e){
            e.printStackTrace();
            try {
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance(FirebaseApp.getInstance("second app")).getReference();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }

        if(mFirebaseDatabaseReference != null && !TextUtils.isEmpty(CHAT_REFERENCE)) {

            final MessageFirebaseAdapter firebaseAdapter = new MessageFirebaseAdapter(context, saveLogiBean.getId().toString(), mFirebaseDatabaseReference.child(CHAT_REFERENCE));
            firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                }
            });

            rv_messages.setLayoutManager(mLinearLayoutManager);
            rv_messages.setAdapter(firebaseAdapter);
        }
    }

    public void setActivity(){
        strSelectedTab = 1;
        binding.vf.setDisplayedChild(0);
        if(activityAdapter==null){
            activityAdapter = new ActivityAdapter(context, preferences.getUserDataPref().getId(), rv_activities, new ActivityAdapter.ActivitiesListner() {
                @Override
                public void onClick(int id, CreatePostBean bean) {
                    Log.w("Go","social adaper");
                    Intent in = new Intent(context, PostDetailActivity.class);
                    in.putExtra("postDetail",bean);
                    startActivity(in);
                }

                @Override
                public void onDeletClick(String id, CreatePostBean bean) {
                    deletePost(id);
                }
            });
            rv_activities.setAdapter(activityAdapter);
        }
        binding.txtSetting.setVisibility(View.VISIBLE);
        binding.txtNew.setVisibility(View.GONE);
        binding.txtActivityTab.setTextColor(getResources().getColor(R.color.white));
        binding.viewActivity.setVisibility(View.VISIBLE);
        binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewMessage.setVisibility(View.GONE);
        binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewFriends.setVisibility(View.GONE);
        binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewRequests.setVisibility(View.GONE);
        getAllActivities();
    }
    public void setMessage(){
        strSelectedTab = 2;
        binding.vf.setDisplayedChild(1);
        binding.txtSetting.setVisibility(View.GONE);
        binding.txtNew.setVisibility(View.VISIBLE);
        binding.txtActivityTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewActivity.setVisibility(View.GONE);
        binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.white));
        binding.viewMessage.setVisibility(View.VISIBLE);
        binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewFriends.setVisibility(View.GONE);
        binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewRequests.setVisibility(View.GONE);
        getMesagesDetail();
    }
    public void setFriends(){
        strSelectedTab = 3;
        binding.vf.setDisplayedChild(2);
        binding.txtSetting.setVisibility(View.GONE);
        binding.txtNew.setVisibility(View.VISIBLE);
        binding.txtActivityTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewActivity.setVisibility(View.GONE);
        binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewMessage.setVisibility(View.GONE);
        binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.white));
        binding.viewFriends.setVisibility(View.VISIBLE);
        binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewRequests.setVisibility(View.GONE);

        rv_customers = (RecyclerView)findViewById(R.id.rv_customers);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_customers.setLayoutManager(linearLayoutManager);

        allFriendsAdapter = new AllFriendsAdapter(context, rv_customers, new AllFriendsAdapter.FriendRequestListner() {
            @Override
            public void onClick(int id, CustomerDetailBean bean) {
                if(id==50001){
                    if(bean.getFriend_id()!=null)
                        showComfirmDialog(String.valueOf(bean.getUser_id()),"Are you sure want to unfriend?","3");
                }
                if (id==50002){
                    final UserModel model = new UserModel();
                    model.setUser_id(String.valueOf(bean.getUser_id()));
                    model.setName(bean.getName()+" "+bean.getlName());
                    model.setFriend_id(String.valueOf(bean.getFriend_id()));
                    model.setPhoto_profile(bean.getProfilePic());
                    Log.w("friendId","From social "+model.getFriend_id());
                    Intent in = new Intent(context,MessageActivity.class);
                    in.putExtra("user_model", model);
                    startActivity(in);
                }
            }
        });

        rv_customers.setAdapter(allFriendsAdapter);
        getAllFriends();
    }
    public void setRequests(){
        strSelectedTab = 4;
        binding.vf.setDisplayedChild(3);
        binding.txtSetting.setVisibility(View.GONE);
        binding.txtNew.setVisibility(View.VISIBLE);
        binding.txtActivityTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewActivity.setVisibility(View.GONE);
        binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewMessage.setVisibility(View.GONE);
        binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.light_gray));
        binding.viewFriends.setVisibility(View.GONE);
        binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.white));
        binding.viewRequests.setVisibility(View.VISIBLE);

        rv_req_customers = (RecyclerView)findViewById(R.id.rv_req_customers);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_req_customers.setLayoutManager(linearLayoutManager);

        friendRequestAdapter = new FriendRequestAdapter(context, rv_req_customers, new FriendRequestAdapter.FriendRequestListner() {
            @Override
            public void onClick(int id, CustomerDetailBean bean) {
                if(id==50001){
                    if(bean.getFriend_id()!=null)
                        showComfirmDialog(String.valueOf(bean.getUser_id()),"Are you sure want to accept friend request?","1");

                }else if(id==50002){
                    if(bean.getFriend_id()!=null)
                        showComfirmDialog(String.valueOf(bean.getUser_id()),"Are you sure want to decline friend request?","2");
                }
            }
        });

        rv_req_customers.setAdapter(friendRequestAdapter);
        getAllRequests();
    }
}
