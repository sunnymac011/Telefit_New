package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.MultiSelectAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityGymBinding;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.SubExerciseBean;
import fit.tele.com.telefit.modelBean.YogaApiBean;
import fit.tele.com.telefit.modelBean.YogaSubCatId;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.OnLoadMoreListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class YogaActivity extends BaseActivity implements View.OnClickListener {

    ActivityGymBinding binding;
    private MultiSelectAdapter multiSelectAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gym;
    }

    @Override
    public void init() {

        binding = (ActivityGymBinding) getBindingObj();
        binding.rvExerciseType.setVisibility(View.INVISIBLE);
        setListner();
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

        binding.txtNext.setOnClickListener(this);
        binding.txtHeaderName.setText("Yoga");

        binding.rvGym.setLayoutManager(new GridLayoutManager(this, 3));

        if (multiSelectAdapter == null) {
            multiSelectAdapter = new MultiSelectAdapter(context, binding.rvGym);
            multiSelectAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                }
            });
        }
        binding.rvGym.setAdapter(multiSelectAdapter);
        multiSelectAdapter.clearAll();

        callSubExerciseApi();
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

            case R.id.txt_next:
                if (multiSelectAdapter != null)
                {
                    if (multiSelectAdapter.getSelected().size() > 0) {
                        Log.e("selected item ",""+multiSelectAdapter.getSelected().toString());

                        ArrayList<YogaSubCatId> arrayList = new ArrayList<>();
                        YogaApiBean yogaApiBean = new YogaApiBean();
                        YogaSubCatId yogaSubCatId;
                        ArrayList<YogaSubCatId> yogaSubCatIds = new ArrayList<>();
                        yogaApiBean.setCatId("4");
                        for (int i=0;i<multiSelectAdapter.getSelected().size();i++) {
                            yogaSubCatId = new YogaSubCatId();
                            yogaSubCatId.setId(multiSelectAdapter.getSelected().get(i).getId());
                            arrayList.add(yogaSubCatId);
                        }
                        yogaApiBean.setSubCatId(arrayList);
                        intent = new Intent(context, ExercisesActivity.class);
                        intent.putExtra("SelectedItems",yogaApiBean);
                        intent.putExtra("from","yoga");
                        startActivity(intent);
                    }
                    else
                        CommonUtils.toast(context,"Please select category!");
                }
                break;
        }
    }

    private void callSubExerciseApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("cat_id", "4");

            Observable<ModelBean<ArrayList<SubExerciseBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getSubExerciseAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<SubExerciseBean>>>() {
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
                        public void onNext(ModelBean<ArrayList<SubExerciseBean>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                if (multiSelectAdapter.getItemCount() <= 0)
                                    multiSelectAdapter.addAllList(exercisesBean.getResult());
                                else {
                                    multiSelectAdapter.removeProgress();
                                    multiSelectAdapter.addAllList(exercisesBean.getResult());
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
}
