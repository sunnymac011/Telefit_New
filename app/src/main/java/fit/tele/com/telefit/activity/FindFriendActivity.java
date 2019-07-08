package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.CustomerDetailAdapterr;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityFindFriendBinding;
import fit.tele.com.telefit.modelBean.CustomerDetailBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FindFriendActivity extends BaseActivity implements View.OnClickListener{

    ActivityFindFriendBinding binding;
    CustomerDetailAdapterr customerDetailAdapterr;
    LinearLayoutManager linearLayoutManager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_find_friend;
    }

    @Override
    public void init() {
        binding = (ActivityFindFriendBinding)getBindingObj();

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvCustomers.setLayoutManager(linearLayoutManager);

        customerDetailAdapterr = new CustomerDetailAdapterr(context, binding.rvCustomers, new CustomerDetailAdapterr.CustomerDetailListner() {
            @Override
            public void onClick(int id, CustomerDetailBean bean) {
                if(id==50001)
                    addFriend(String.valueOf(bean.getId()));
            }
        });

        binding.rvCustomers.setAdapter(customerDetailAdapterr);


        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customerDetailAdapterr.filter(binding.edtSearch.getText().toString());
              //  binding.txtTotalExercises.setText(exercisesAdapter.getItemCount()+" exercises");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setListner();

        getAllCustomers();


    }

    public void setListner() {
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
        binding.llSocial.setOnClickListener(this);

    }

    private void getAllCustomers() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();

            Observable<ModelBean<ArrayList<CustomerDetailBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getAllCustomers(map);
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
                                customerDetailAdapterr.addAllList(loginBean.getResult());

                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void addFriend(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("friend_id",id);

            Observable<ModelBean<ArrayList<CustomerDetailBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).addFriend(map);
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
                            CommonUtils.toast(context,loginBean.getMessage());
                            binding.progress.setVisibility(View.GONE);
                            if(loginBean.getStatus()==1){


                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
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
                finish();
                break;
        }
    }
}
