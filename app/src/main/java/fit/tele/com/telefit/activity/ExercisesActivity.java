package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExercisesAdapter;
import fit.tele.com.telefit.adapter.ExercisesTestAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityExercisesBinding;
import fit.tele.com.telefit.dialog.CustomDialog;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.PaymentInfoBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.SelectedItemsBean;
import fit.tele.com.telefit.modelBean.YogaApiBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.OnLoadMoreListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExercisesActivity extends BaseActivity implements View.OnClickListener {

    ActivityExercisesBinding binding;
    ExercisesAdapter exercisesAdapter;
    ExercisesTestAdapter exercisesTestAdapter;
    private SelectedItemsBean selectedItemsBean;
    private YogaApiBean yogaApiBean;
    private String strFrom="",strFromSub="";
    private ArrayList<ExercisesListBean> exercisesListBeans;
    private RoutinePlanDetailsBean routinePlanDetailsBean;
    ExxDetial exxDetial = new ExxDetial();
    private CustomDialog customDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_exercises;
    }

    private void setListner() {
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);

        if (exercisesAdapter == null) {
            exercisesAdapter = new ExercisesAdapter(context, binding.rvExercises, new ExercisesAdapter.ClickListener() {
                @Override
                public void onClick(String exe_id, ExercisesListBean exercisesListBean, boolean isLongClick) {
                    if (preferences.getIsUpdatePlan() != null && !TextUtils.isEmpty(preferences.getIsUpdatePlan()))
                    {
                        if (!isLongClick) {
                            if (preferences.getUpdateRoutineDataPref() != null) {
                                Gson gson = new Gson();
                                routinePlanDetailsBean = gson.fromJson(preferences.getUpdateRoutineDataPref(), new TypeToken<RoutinePlanDetailsBean>(){}.getType());
                            }
                            else
                                routinePlanDetailsBean = new RoutinePlanDetailsBean();

                            exxDetial.setUserId(preferences.getUserDataPref().getId().toString().trim());
                            exxDetial.setId(preferences.getIsUpdatePlan().trim());
                            exxDetial.setExes(exercisesListBean);
                            routinePlanDetailsBean.getExxDetial().add(exxDetial);
                            preferences.saveUpdateRoutineData(routinePlanDetailsBean);
                            CommonUtils.toast(context,"Exercise added to routine." );

                            Intent intent = new Intent(context, UpdateRoutineActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(context, ExerciseDetailsActivity.class);
                            intent.putExtra("From", "ExercisesActivity");
                            intent.putExtra("ExerciseDetails", exe_id);
                            intent.putExtra("ExercisesListBean", exercisesListBean);
                            startActivity(intent);
                        }
                    }
                    else {
                        if (!isLongClick) {
                            if (preferences.getRoutineDataPref() != null) {
                                Gson gson = new Gson();
                                exercisesListBeans = gson.fromJson(preferences.getRoutineDataPref(), new TypeToken<List<ExercisesListBean>>(){}.getType());
                            }
                            else
                                exercisesListBeans = new ArrayList<>();

                            exercisesListBeans.add(exercisesListBean);
                            preferences.saveRoutineData(exercisesListBeans);
                            CommonUtils.toast(context,"Exercise added to routine." );
                        }

                        callCheckPaymentApi(isLongClick, exe_id, exercisesListBean);
                    }
                }
            });

            exercisesAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                }
            });


        }
        binding.rvExercises.setAdapter(exercisesAdapter);
        exercisesAdapter.clearAll();
    }

    @Override
    public void init() {
        binding = (ActivityExercisesBinding) getBindingObj();

        setListner();
        Intent intent = getIntent();
        if(getIntent() != null && getIntent().hasExtra("from"))
            strFrom = intent.getStringExtra("from");

        if (strFrom.equalsIgnoreCase("gym")) {
            if(getIntent() != null && getIntent().hasExtra("SelectedItems"))
                selectedItemsBean = intent.getParcelableExtra("SelectedItems");
            if (selectedItemsBean != null)
                callExerciseApi();
        }
        else if (strFrom.equalsIgnoreCase("yoga")) {
            if(getIntent() != null && getIntent().hasExtra("SelectedItems"))
                yogaApiBean = intent.getParcelableExtra("SelectedItems");
            if (yogaApiBean != null)
                callYogaExerciseApi();
        }
        else if (strFrom.equalsIgnoreCase("crossfit")) {
            if(getIntent() != null && getIntent().hasExtra("fromSub"))
                strFromSub = intent.getStringExtra("fromSub");

            if (strFromSub.equalsIgnoreCase("TheGirl")) {
                if(getIntent() != null && getIntent().hasExtra("SelectedItems"))
                    selectedItemsBean = intent.getParcelableExtra("SelectedItems");
                if (selectedItemsBean != null)
                    callCrossFitExerciseApi();
            }
            else {
                if(getIntent() != null && getIntent().hasExtra("SelectedItems"))
                    yogaApiBean = intent.getParcelableExtra("SelectedItems");
                if (yogaApiBean != null)
                    callCrossFitExerciseApi();
            }
        }

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                exercisesAdapter.filter(binding.edtSearch.getText().toString());
                binding.txtTotalExercises.setText(exercisesAdapter.getItemCount()+" exercises");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        }
    }

    private void callExerciseApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<ExercisesListBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getFilteredExercises(selectedItemsBean);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ExercisesListBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callExerciseApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ExercisesListBean>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                binding.txtTotalExercises.setText(exercisesBean.getResult().size()+" exercises");
                                if (exercisesAdapter.getItemCount() <= 0)
                                    exercisesAdapter.addAllList(exercisesBean.getResult());
                                else {
                                    exercisesAdapter.removeProgress();
                                    exercisesAdapter.addAllList(exercisesBean.getResult());
                                }
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callYogaExerciseApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<ExercisesListBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getYogaFilteredExercises(yogaApiBean);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ExercisesListBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callExerciseApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ExercisesListBean>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                binding.txtTotalExercises.setText(exercisesBean.getResult().size()+" exercises");
                                if (exercisesAdapter.getItemCount() <= 0)
                                    exercisesAdapter.addAllList(exercisesBean.getResult());
                                else {
                                    exercisesAdapter.removeProgress();
                                    exercisesAdapter.addAllList(exercisesBean.getResult());
                                }
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callCrossFitExerciseApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<ExercisesListBean>>> signupusers;
            if (strFromSub.equalsIgnoreCase("TheGirl"))
                signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCrossFitFilteredExercises(selectedItemsBean);
            else
                signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCrossFitFilteredExercises(yogaApiBean);

            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ExercisesListBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCrossFitExerciseApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ExercisesListBean>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                binding.txtTotalExercises.setText(exercisesBean.getResult().size()+" exercises");
                                if (exercisesAdapter.getItemCount() <= 0)
                                    exercisesAdapter.addAllList(exercisesBean.getResult());
                                else {
                                    exercisesAdapter.removeProgress();
                                    exercisesAdapter.addAllList(exercisesBean.getResult());
                                }
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callCheckPaymentApi(boolean isLongClick, String exe_id,ExercisesListBean exercisesListBean) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<PaymentInfoBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCheckPaymentAPI();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<PaymentInfoBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCheckPaymentApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<PaymentInfoBean> apiPaymentInfo) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiPaymentInfo.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                if (apiPaymentInfo.getResult().getIsPackage().equalsIgnoreCase("no")){
                                    Intent intent = new Intent(ExercisesActivity.this,PaymentDescActivity.class);
                                    intent.putExtra("paymentInfo",apiPaymentInfo.getResult());
                                    startActivity(intent);
                                }
                                else {
                                    if (isLongClick) {
                                        Intent intent = new Intent(context, ExerciseDetailsActivity.class);
                                        intent.putExtra("From", "ExercisesActivity");
                                        intent.putExtra("ExerciseDetails", exe_id);
                                        intent.putExtra("ExercisesListBean", exercisesListBean);
                                        startActivity(intent);
                                    }
                                }
                            }
                            else
                                CommonUtils.toast(context, ""+apiPaymentInfo.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
