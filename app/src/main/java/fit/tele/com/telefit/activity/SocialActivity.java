package fit.tele.com.telefit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.FriendRequestAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySocialBinding;
import fit.tele.com.telefit.modelBean.CustomerDetailBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocialActivity extends BaseActivity implements View.OnClickListener{

    ActivitySocialBinding binding;
    private int strSelectedTab = 1;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_customers;

    FriendRequestAdapter friendRequestAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_social;
    }

    @Override
    public void init() {
        binding = (ActivitySocialBinding)getBindingObj();
        setListner();
    }

    public void setListner(){
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);

        binding.llActivityTab.setOnClickListener(this);
        binding.llMessagesTab.setOnClickListener(this);
        binding.llFriendsTab.setOnClickListener(this);
        binding.llRequestsTab.setOnClickListener(this);
        binding.txtNew.setOnClickListener(this);

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


            case R.id.ll_activity_tab:
                strSelectedTab = 1;
                binding.vf.setDisplayedChild(0);
                binding.txtActivityTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewActivity.setVisibility(View.VISIBLE);
                binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMessage.setVisibility(View.GONE);
                binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFriends.setVisibility(View.GONE);
                binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewRequests.setVisibility(View.GONE);
               // setSearchFoodData();

                break;

            case R.id.ll_messages_tab:
                strSelectedTab = 2;
                binding.vf.setDisplayedChild(1);
                binding.txtActivityTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewActivity.setVisibility(View.GONE);
                binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewMessage.setVisibility(View.VISIBLE);
                binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFriends.setVisibility(View.GONE);
                binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewRequests.setVisibility(View.GONE);
                break;

            case R.id.ll_friends_tab:
                strSelectedTab = 3;
                binding.vf.setDisplayedChild(2);

                binding.txtActivityTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewActivity.setVisibility(View.GONE);
                binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMessage.setVisibility(View.GONE);
                binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewFriends.setVisibility(View.VISIBLE);
                binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewRequests.setVisibility(View.GONE);

                break;

            case R.id.ll_requests_tab:

                strSelectedTab = 4;
                binding.vf.setDisplayedChild(3);

                binding.txtActivityTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewActivity.setVisibility(View.GONE);
                binding.txtMessagesTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMessage.setVisibility(View.GONE);
                binding.txtFriendsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFriends.setVisibility(View.GONE);
                binding.txtRequestsTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewRequests.setVisibility(View.VISIBLE);

                rv_customers = (RecyclerView)findViewById(R.id.rv_customers);
                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                rv_customers.setLayoutManager(linearLayoutManager);

               friendRequestAdapter = new FriendRequestAdapter(context, rv_customers, new FriendRequestAdapter.FriendRequestListner() {
                   @Override
                   public void onClick(int id, CustomerDetailBean bean) {
                        if(id==50001){
                            if(bean.getFriend_id()!=null)
                                showComfirmDialog(String.valueOf(bean.getFriend_id()),"Are you sure want to accept friend request?");

                        }else if(id==50002){
                            if(bean.getFriend_id()!=null)
                                showComfirmDialog(String.valueOf(bean.getFriend_id()),"Are you sure want to decline friend request?");
                        }
                   }
               });

                rv_customers.setAdapter(friendRequestAdapter);

                getAllRequests();

                break;

            case R.id.txt_new:

                Intent in = new Intent(SocialActivity.this,FindFriendActivity.class);
                startActivity(in);

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
                            if(loginBean.getStatus()==1){
                                binding.progress.setVisibility(View.GONE);
                                friendRequestAdapter.addAllList(loginBean.getResult());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void acceptRequest(String friendId) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("friend_id",friendId);

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
                            if(loginBean.getStatus()==1){
                                binding.progress.setVisibility(View.GONE);
                                friendRequestAdapter.addAllList(loginBean.getResult());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    public void showComfirmDialog(String friendId,String message){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle(message);
            // Add the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    acceptRequest(friendId);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            builder.create().show();

    }
}
