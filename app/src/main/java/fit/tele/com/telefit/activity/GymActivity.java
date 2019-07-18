package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExercisesTypeAdapter;
import fit.tele.com.telefit.adapter.MultiSelectOptionsAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityGymBinding;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.SelectedItemsBean;
import fit.tele.com.telefit.modelBean.SubCatId;
import fit.tele.com.telefit.modelBean.SubExerciseBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GymActivity extends BaseActivity implements View.OnClickListener {

    ActivityGymBinding binding;
    private MultiSelectOptionsAdapter multiSelectOptionsAdapter;
    private ExercisesTypeAdapter exercisesTypeAdapter;
    private String strCatId="";
    private ArrayList<SubExerciseBean> subExerciseBean = new ArrayList<>();
    private SelectedItemsBean selectedItemsBean = new SelectedItemsBean();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gym;
    }

    @Override
    public void init() {
        binding = (ActivityGymBinding) getBindingObj();

        setListner();
    }

    private void setListner() {

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("exerciseType"))
            strCatId = intent.getStringExtra("exerciseType");

        if (strCatId != null && !TextUtils.isEmpty(strCatId))
        {
            if (strCatId.equalsIgnoreCase("1"))
                binding.txtHeaderName.setText("Gym");
            else
                binding.txtHeaderName.setText("HIIT");
        }

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

        binding.rvGym.setLayoutManager(new GridLayoutManager(this, 3));

        if (multiSelectOptionsAdapter == null)
            multiSelectOptionsAdapter = new MultiSelectOptionsAdapter(context, binding.rvGym);

        binding.rvGym.setAdapter(multiSelectOptionsAdapter);
        multiSelectOptionsAdapter.clearAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvExerciseType.setLayoutManager(linearLayoutManager);

        if (exercisesTypeAdapter == null) {
            exercisesTypeAdapter = new ExercisesTypeAdapter(context, binding.rvExerciseType, new ExercisesTypeAdapter.ExerciseListener() {
                @Override
                public void onClick(String id, String pre_id) {
                    for (int i=0;i<subExerciseBean.size();i++) {
                        if (subExerciseBean.get(i).getId().equalsIgnoreCase(pre_id)) {
                            if (multiSelectOptionsAdapter.getAll().size() > 0) {
                                subExerciseBean.get(i).getApiSubcatoptExe().clear();
                                subExerciseBean.get(i).getApiSubcatoptExe().addAll(multiSelectOptionsAdapter.getAll());
                            }
                        }
                    }
                    for (int i=0;i<subExerciseBean.size();i++) {
                        if (subExerciseBean.get(i).getId().equalsIgnoreCase(id)) {
                            if (subExerciseBean.get(i).getApiSubcatoptExe().size() > 0)
                            {
                                multiSelectOptionsAdapter.clearAll();
                                multiSelectOptionsAdapter.addAllList(subExerciseBean.get(i).getApiSubcatoptExe());
                            }
                        }
                    }
                }
            });
        }
        binding.rvExerciseType.setAdapter(exercisesTypeAdapter);
        exercisesTypeAdapter.clearAll();

        callSubExerciseApi(strCatId);
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
                if (multiSelectOptionsAdapter != null)
                {
                    ArrayList<String> arrayList;
                    SubCatId subCatId;
                    ArrayList<SubCatId> subCatIds = new ArrayList<>();
                    selectedItemsBean.setCatId(strCatId);
                    for (int i=0;i<subExerciseBean.size();i++) {
                        arrayList = new ArrayList<>();
                        subCatId = new SubCatId();
                        for (int j=0;j<subExerciseBean.get(i).getApiSubcatoptExe().size();j++) {
                            if (subExerciseBean.get(i).getApiSubcatoptExe().get(j).isCheck())
                                arrayList.add(subExerciseBean.get(i).getApiSubcatoptExe().get(j).getId());
                        }
                        if (arrayList.size() > 0) {
                            subCatId.setId(subExerciseBean.get(i).getId());
                            subCatId.setOptSub(arrayList);
                            subCatIds.add(subCatId);
                            selectedItemsBean.setSubCatId(subCatIds);
                        }
                    }
                    if (selectedItemsBean.getSubCatId() != null && selectedItemsBean.getSubCatId().size() > 0)
                    {
                        intent = new Intent(GymActivity.this,ExercisesActivity.class);
                        intent.putExtra("SelectedItems",selectedItemsBean);
                        intent.putExtra("from","gym");
                        startActivity(intent);
                    }
                    else
                        CommonUtils.toast(context,"Please selecte category!");
                }
                break;
        }
    }

    private void callSubExerciseApi(String cat_id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("cat_id", cat_id);

            Observable<ModelBean<ArrayList<SubExerciseBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getGymExerciseAPI(map);
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
                                subExerciseBean = exercisesBean.getResult();
                                if (exercisesTypeAdapter.getItemCount() <= 0)
                                    exercisesTypeAdapter.addAllList(exercisesBean.getResult());
                                else {
                                    exercisesTypeAdapter.removeProgress();
                                    exercisesTypeAdapter.addAllList(exercisesBean.getResult());
                                }
                                if (exercisesBean.getResult().get(0).getApiSubcatoptExe().size() > 0)
                                    multiSelectOptionsAdapter.addAllList(exercisesBean.getResult().get(0).getApiSubcatoptExe());
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
