package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.CrossFitAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCrossfitBinding;
import fit.tele.com.telefit.modelBean.CrossFitBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.SelectedItemsBean;
import fit.tele.com.telefit.modelBean.SubCatId;
import fit.tele.com.telefit.modelBean.SubExerciseBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;
import fit.tele.com.telefit.modelBean.YogaApiBean;
import fit.tele.com.telefit.modelBean.YogaSubCatId;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CrossFitActivity extends BaseActivity implements View.OnClickListener {

    ActivityCrossfitBinding binding;
    private CrossFitAdapter crossFitAdapter;
    private SelectedItemsBean selectedItemsBean = new SelectedItemsBean();
    private ArrayList<CrossFitBean> crossFitBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_crossfit;
    }

    @Override
    public void init() {
        binding = (ActivityCrossfitBinding) getBindingObj();
        setListner();

    }

    private void setListner() {
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvCrossfit.setLayoutManager(linearLayoutManager);

        if (crossFitAdapter == null)
        {
            crossFitAdapter = new CrossFitAdapter(context, binding.rvCrossfit, new CrossFitAdapter.ClickListener() {
                @Override
                public void onClick(String cat_id, String cat_name, ArrayList<SubOptionsBean> subOptionsBeans) {
                    Intent intent;
                    if (cat_name.equalsIgnoreCase("THE GIRLS"))
                    {
                        intent = new Intent(CrossFitActivity.this,GirlsActivity.class);
                        intent.putExtra("subOptionsBeans",subOptionsBeans);
                        intent.putExtra("subCatId",cat_id);
                        startActivity(intent);
                    }
                    else {
                        ArrayList<YogaSubCatId> arrayList = new ArrayList<>();
                        YogaApiBean yogaApiBean = new YogaApiBean();
                        YogaSubCatId yogaSubCatId;
                        yogaApiBean.setCatId("2");
                        yogaSubCatId = new YogaSubCatId();
                        yogaSubCatId.setId(cat_id);
                        arrayList.add(yogaSubCatId);
                        yogaApiBean.setSubCatId(arrayList);
                        Log.e("yogaApiBean",""+yogaApiBean.toString());
                        intent = new Intent(context, ExercisesActivity.class);
                        intent.putExtra("SelectedItems",yogaApiBean);
                        intent.putExtra("from","crossfit");
                        startActivity(intent);
                    }
                }
            });
        }

        binding.rvCrossfit.setAdapter(crossFitAdapter);
        crossFitAdapter.clearAll();

        callSubExerciseApi();
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
            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;
            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;
        }
    }

    private void callSubExerciseApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("cat_id", "2");

            Observable<ModelBean<ArrayList<CrossFitBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCrossFitAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CrossFitBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callEmailLoginApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<CrossFitBean>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                crossFitBean = exercisesBean.getResult();
                                crossFitAdapter.addAllList(exercisesBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
