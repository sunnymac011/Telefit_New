package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.RoutinePlanAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityFitnessBinding;
import fit.tele.com.telefit.modelBean.CategoryBean;
import fit.tele.com.telefit.modelBean.ExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.PaymentInfoBean;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;
import fit.tele.com.telefit.modelBean.RoutinePlanListBean;
import fit.tele.com.telefit.modelBean.YogaExerciseDetailsBean;
import fit.tele.com.telefit.themes.MainActivityTheme;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FitnessActivity extends BaseActivity implements View.OnClickListener {

    ActivityFitnessBinding binding;
    private RoutinePlanAdapter routinePlanAdapter;
    private RoutinePlanAdapter pastRoutinePlanAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_fitness;
    }

    @Override
    public void init() {
        binding = (ActivityFitnessBinding) getBindingObj();
        setListner();
    }

    private void setListner() {
        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);

        binding.llAddPlan.setOnClickListener(this);
        binding.txtExplore.setOnClickListener(this);

        binding.imgCrossfit.setOnClickListener(this);
        binding.imgGym.setOnClickListener(this);
        binding.imgHiit.setOnClickListener(this);
        binding.imgYoga.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvFuturePlans.setLayoutManager(linearLayoutManager);
        binding.rvPastPlans.setLayoutManager(linearLayoutManager1);

        if (routinePlanAdapter == null) {
            routinePlanAdapter = new RoutinePlanAdapter(context, binding.rvFuturePlans, new RoutinePlanAdapter.ClickListener() {
                @Override
                public void onClick(String plan_id) {
                    Intent intent = new Intent(FitnessActivity.this,PlayVideoActivity.class);
                    intent.putExtra("plan_id",plan_id);
                    startActivity(intent);
                }
            });
        }
        binding.rvFuturePlans.setAdapter(routinePlanAdapter);
        routinePlanAdapter.clearAll();

        if (pastRoutinePlanAdapter == null) {
            pastRoutinePlanAdapter = new RoutinePlanAdapter(context, binding.rvPastPlans, new RoutinePlanAdapter.ClickListener() {
                @Override
                public void onClick(String plan_id) {
                    Intent intent = new Intent(FitnessActivity.this,PlayVideoActivity.class);
                    intent.putExtra("plan_id",plan_id);
                    startActivity(intent);
                }
            });
        }
        binding.rvPastPlans.setAdapter(pastRoutinePlanAdapter);
        pastRoutinePlanAdapter.clearAll();

        callRoutinePlanApi();
        callCategoryApi();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
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

            case R.id.ll_goals:
                intent = new Intent(context, GoalsActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_add_plan:
//                intent = new Intent(context, CreateRoutineActivity.class);
//                startActivity(intent);
                callCheckPaymentApi();
                break;

            case R.id.txt_explore:
                intent = new Intent(context, MainExercisesActivity.class);
                startActivity(intent);
                break;

            case R.id.img_gym:
                intent = new Intent(context, GymActivity.class);
                intent.putExtra("exerciseType","1");
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.img_crossfit:
                intent = new Intent(context, CrossFitActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.img_hiit:
                intent = new Intent(context, GymActivity.class);
                intent.putExtra("exerciseType","3");
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.img_yoga:
                intent = new Intent(context, YogaActivity.class);
                intent.putExtra("exerciseType","4");
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;
        }
    }

    private void callRoutinePlanApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<RoutinePlanListBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getRoutinePlansApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<RoutinePlanListBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<RoutinePlanListBean> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                if (apiExercisesBean.getResult() != null) {
                                    if (apiExercisesBean.getResult().getFuture().size() > 0)
                                    {
                                        routinePlanAdapter.addAllList(apiExercisesBean.getResult().getFuture());
                                        binding.llFuture.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        binding.rvFuturePlans.setVisibility(View.GONE);
                                        binding.llFuture.setVisibility(View.GONE);
                                    }
                                    if (apiExercisesBean.getResult().getPast().size() > 0)
                                    {
                                        pastRoutinePlanAdapter.addAllList(apiExercisesBean.getResult().getFuture());
                                        binding.llPast.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        binding.rvPastPlans.setVisibility(View.GONE);
                                        binding.llPast.setVisibility(View.GONE);
                                    }
                                }
                            }
                            else
                                CommonUtils.toast(context, ""+apiExercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callCheckPaymentApi() {
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
                                    Intent intent = new Intent(FitnessActivity.this,PaymentDescActivity.class);
                                    intent.putExtra("paymentInfo",apiPaymentInfo.getResult());
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(FitnessActivity.this,CreateRoutineActivity.class);
                                    startActivity(intent);
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

    private void callCategoryApi() {
        if (CommonUtils.isInternetOn(context)) {

            Observable<ModelBean<ArrayList<CategoryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCategoryApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CategoryBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCategoryApi"," "+e);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<CategoryBean>> apiExercisesBean) {
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                for (int i=0; i<apiExercisesBean.getResult().size();i++) {
                                    if (apiExercisesBean.getResult().get(i).getCatName().equalsIgnoreCase("CrossFit")) {
                                        if (apiExercisesBean.getResult() != null && apiExercisesBean.getResult().get(i).getCatImageUrl() != null && !TextUtils.isEmpty(apiExercisesBean.getResult().get(i).getCatImageUrl())) {
                                            Picasso.with(context)
                                                    .load(apiExercisesBean.getResult().get(i).getCatImageUrl())
                                                    .transform(new CircleTransform())
                                                    .resize(100, 100)
                                                    .onlyScaleDown()
                                                    .into(binding.imgCrossfit);
                                        }
                                    }
                                    if (apiExercisesBean.getResult().get(i).getCatName().equalsIgnoreCase("gym")) {
                                        if (apiExercisesBean.getResult() != null && apiExercisesBean.getResult().get(i).getCatImageUrl() != null && !TextUtils.isEmpty(apiExercisesBean.getResult().get(i).getCatImageUrl())) {
                                            Picasso.with(context)
                                                    .load(apiExercisesBean.getResult().get(i).getCatImageUrl())
                                                    .transform(new CircleTransform())
                                                    .resize(100, 100)
                                                    .onlyScaleDown()
                                                    .into(binding.imgGym);
                                        }
                                    }
                                    if (apiExercisesBean.getResult().get(i).getCatName().equalsIgnoreCase("hiit")) {
                                        if (apiExercisesBean.getResult() != null && apiExercisesBean.getResult().get(i).getCatImageUrl() != null && !TextUtils.isEmpty(apiExercisesBean.getResult().get(i).getCatImageUrl())) {
                                            Picasso.with(context)
                                                    .load(apiExercisesBean.getResult().get(i).getCatImageUrl())
                                                    .transform(new CircleTransform())
                                                    .resize(100, 100)
                                                    .onlyScaleDown()
                                                    .into(binding.imgHiit);
                                        }
                                    }
                                    if (apiExercisesBean.getResult().get(i).getCatName().equalsIgnoreCase("yoga")) {
                                        if (apiExercisesBean.getResult() != null && apiExercisesBean.getResult().get(i).getCatImageUrl() != null && !TextUtils.isEmpty(apiExercisesBean.getResult().get(i).getCatImageUrl())) {
                                            Picasso.with(context)
                                                    .load(apiExercisesBean.getResult().get(i).getCatImageUrl())
                                                    .transform(new CircleTransform())
                                                    .resize(100, 100)
                                                    .onlyScaleDown()
                                                    .into(binding.imgYoga);
                                        }
                                    }
                                }
                            }
                            else
                                CommonUtils.toast(context, ""+apiExercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
