package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExeVideoAdapter;
import fit.tele.com.telefit.adapter.GirlAdapter;
import fit.tele.com.telefit.adapter.SubCatAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityExerciseDetailsBinding;
import fit.tele.com.telefit.dialog.SetTimeDialog;
import fit.tele.com.telefit.dialog.SetsRepsDialog;
import fit.tele.com.telefit.dialog.VideoDialog;
import fit.tele.com.telefit.modelBean.ExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.SubCatId;
import fit.tele.com.telefit.modelBean.VideoArrayBean;
import fit.tele.com.telefit.modelBean.YogaExerciseDetailsBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.MyTagHandler;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExerciseDetailsActivity extends BaseActivity implements View.OnClickListener {

    ActivityExerciseDetailsBinding binding;
    ExerciseDetailsBean exercisesBean;
    YogaExerciseDetailsBean yogaExerciseDetailsBean;
    String exerciseId="", strFrom="";
    HashMap<String, String> map;
    VideoDialog videoDialog;
    ExeVideoAdapter exeVideoAdapter;
    ExercisesListBean exercisesListBean;
    ArrayList<ExercisesListBean> exercisesListBeans;
    private RoutinePlanDetailsBean routinePlanDetailsBean;
    private SetTimeDialog setTimeDialog;
    private boolean isExerciseTime = false;
    private SubCatAdapter subCatAdapter;
    private ExxDetial exxDetial = new ExxDetial();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_exercise_details;
    }

    @Override
    public void init() {
        map = new HashMap<>();
        binding = (ActivityExerciseDetailsBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.txtAdd.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null) {
            if (intent.hasExtra("From"))
                strFrom = intent.getStringExtra("From");
            if (intent.hasExtra("ExerciseDetails"))
                exerciseId = intent.getStringExtra("ExerciseDetails");
            if(getIntent() != null && getIntent().hasExtra("ExercisesListBean"))
                exercisesListBean = intent.getParcelableExtra("ExercisesListBean");
        }

        if (exercisesListBean.getCatId().equalsIgnoreCase("1") || exercisesListBean.getCatId().equalsIgnoreCase("3"))
        {
            isExerciseTime = false;
            callExerciseDetailsApi(exerciseId);
        }
        else if (exercisesListBean.getCatId().equalsIgnoreCase("4"))
        {
            isExerciseTime = true;
            callYogaExerciseDetailsApi(exerciseId);
        }
        else if (exercisesListBean.getCatId().equalsIgnoreCase("2"))
        {
            isExerciseTime = true;
            callCrossFitExerciseDetailsApi(exerciseId);
        }

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

            case R.id.txt_add:
                addExercise();
                break;
        }
    }

    private void setData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvSubCat.setLayoutManager(linearLayoutManager);
        binding.rvVideo.setVisibility(View.GONE);
        if (subCatAdapter == null)
        {
            subCatAdapter = new SubCatAdapter(context);
        }
        binding.rvSubCat.setAdapter(subCatAdapter);
        subCatAdapter.clearAll();

        if(exercisesBean != null || yogaExerciseDetailsBean != null) {
            binding.txtExerciseDetails.setMovementMethod(new ScrollingMovementMethod());
            if (exercisesListBean.getCatId().equalsIgnoreCase("4")) {
                binding.llInstruction.setVisibility(View.GONE);
                subCatAdapter.addAllList(yogaExerciseDetailsBean.getOptArray());
//                binding.llMuscle.setVisibility(View.GONE);
//                binding.txtEquipment.setText("Category");
//                if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getOptArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName()))
//                    binding.txtEquipmentType.setText(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName());
//                if (yogaExerciseDetailsBean.getExeTitle() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeTitle()))
//                    binding.txtExerciseType.setText(yogaExerciseDetailsBean.getCatName());

                if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getVideoArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getVideoArray().get(0).getExeImageUrl())) {
                    Picasso.with(this)
                            .load(yogaExerciseDetailsBean.getVideoArray().get(0).getExeImageUrl())
                            .placeholder(R.color.light_gray)
                            .error(R.color.light_gray)
                            .into(binding.imgPreview);
                }

                if (yogaExerciseDetailsBean.getExeTitle() != null && yogaExerciseDetailsBean.getExeTitle() != null
                        && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeTitle()))
                    binding.txtExerciseName.setText(yogaExerciseDetailsBean.getExeTitle());

                if (yogaExerciseDetailsBean.getExeDesc() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeDesc()))
                    binding.txtExerciseDetails.setText(Html.fromHtml(""+Html.fromHtml(yogaExerciseDetailsBean.getExeDesc(), null, new MyTagHandler()), null, new MyTagHandler()));

                binding.imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getVideoArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getVideoArray().get(0).getExeVideoUrl())) {
                            videoDialog = new VideoDialog(context,yogaExerciseDetailsBean.getVideoArray().get(0).getExeVideoUrl());
                            videoDialog.show();
                        }
                        else
                            CommonUtils.toast(context, "No Video Found!");
                    }
                });
            }
            else if (exercisesListBean.getCatId().equalsIgnoreCase("2")) {
                binding.rvVideo.setVisibility(View.VISIBLE);
                subCatAdapter.addAllList(yogaExerciseDetailsBean.getOptArray());
//                binding.llMuscle.setVisibility(View.GONE);
//                binding.txtEquipment.setText("Category");
//                if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getOptArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName()))
//                    binding.txtEquipmentType.setText(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName());
//                if (yogaExerciseDetailsBean.getExeTitle() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeTitle()))
//                    binding.txtExerciseType.setText(yogaExerciseDetailsBean.getCatName());

                if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getVideoArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getVideoArray().get(0).getExeImageUrl())) {
                    Picasso.with(this)
                            .load(yogaExerciseDetailsBean.getVideoArray().get(0).getExeImageUrl())
                            .placeholder(R.color.light_gray)
                            .error(R.color.light_gray)
                            .into(binding.imgPreview);
                }

                if (yogaExerciseDetailsBean.getExeTitle() != null && yogaExerciseDetailsBean.getExeTitle() != null
                        && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeTitle()))
                    binding.txtExerciseName.setText(yogaExerciseDetailsBean.getExeTitle());

                if (yogaExerciseDetailsBean.getExeDesc() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeDesc()))
                    binding.txtExerciseDetails.setText(Html.fromHtml(""+Html.fromHtml(yogaExerciseDetailsBean.getExeDesc(), null, new MyTagHandler()), null, new MyTagHandler()));

                if (yogaExerciseDetailsBean.getExeInstructions() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeInstructions()))
                {
                    binding.llInstruction.setVisibility(View.VISIBLE);
                    binding.txtExerciseIntruction.setText(Html.fromHtml(""+Html.fromHtml(yogaExerciseDetailsBean.getExeInstructions(), null, new MyTagHandler()), null, new MyTagHandler()));
                }

                binding.imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getVideoArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getVideoArray().get(0).getExeVideoUrl())) {
                            videoDialog = new VideoDialog(context,yogaExerciseDetailsBean.getVideoArray().get(0).getExeVideoUrl());
                            videoDialog.show();
                        }
                        else
                            CommonUtils.toast(context, "No Video Found!");
                    }
                });

                binding.rvVideo.setLayoutManager(new GridLayoutManager(this, 2));
                if (exeVideoAdapter == null) {
                    exeVideoAdapter = new ExeVideoAdapter(context, binding.rvVideo, new ExeVideoAdapter.ClickListener() {
                        @Override
                        public void onClick(VideoArrayBean videoArrayBean) {
                            if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getVideoArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getVideoArray().get(0).getExeImageUrl())) {
                                Picasso.with(context)
                                        .load(videoArrayBean.getExeImageUrl())
                                        .placeholder(R.color.light_gray)
                                        .error(R.color.light_gray)
                                        .into(binding.imgPreview);
                            }

                            binding.imgPlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (videoArrayBean != null && !TextUtils.isEmpty(videoArrayBean.getExeVideoUrl())) {
                                        videoDialog = new VideoDialog(context,videoArrayBean.getExeVideoUrl());
                                        videoDialog.show();
                                    }
                                    else
                                        CommonUtils.toast(context, "No Video Found!");
                                }
                            });
                        }
                    });

                    binding.rvVideo.setAdapter(exeVideoAdapter);
                    exeVideoAdapter.clearAll();

                    exeVideoAdapter.addAllList(yogaExerciseDetailsBean.getVideoArray());
                }

            }
            else {
                binding.llInstruction.setVisibility(View.GONE);
                binding.rvVideo.setVisibility(View.GONE);
                subCatAdapter.addAllList(exercisesBean.getSubArray());
//                binding.llMuscle.setVisibility(View.VISIBLE);
//                binding.txtEquipment.setText("Equipment");
//                if (exercisesBean.getSubArray().get(0).getSubCatOption() != null && !TextUtils.isEmpty(exercisesBean.getSubArray().get(0).getSubCatOption()))
//                    binding.txtMuscleType.setText(exercisesBean.getSubArray().get(0).getSubCatOption());
//                if (exercisesBean.getSubArray().get(1).getSubCatOption() != null && !TextUtils.isEmpty(exercisesBean.getSubArray().get(1).getSubCatOption()))
//                    binding.txtExerciseType.setText(exercisesBean.getSubArray().get(1).getSubCatOption());
//                if (exercisesBean.getSubArray().get(2).getSubCatOption() != null && !TextUtils.isEmpty(exercisesBean.getSubArray().get(2).getSubCatOption()))
//                    binding.txtEquipmentType.setText(exercisesBean.getSubArray().get(2).getSubCatOption());

                if (exercisesBean.getExeImageUrl() != null && !TextUtils.isEmpty(exercisesBean.getExeImageUrl())) {
                    Picasso.with(this)
                            .load(exercisesBean.getExeImageUrl())
                            .placeholder(R.color.light_gray)
                            .error(R.color.light_gray)
                            .into(binding.imgPreview);
                }

                if (exercisesBean.getExeTitle() != null && exercisesBean.getExeTitle() != null
                        && !TextUtils.isEmpty(exercisesBean.getExeTitle()))
                    binding.txtExerciseName.setText(exercisesBean.getExeTitle());

                if (exercisesBean.getExeDesc() != null && !TextUtils.isEmpty(exercisesBean.getExeDesc()))
                    binding.txtExerciseDetails.setText(Html.fromHtml(""+Html.fromHtml(exercisesBean.getExeDesc(), null, new MyTagHandler()), null, new MyTagHandler()));

                binding.imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoDialog = new VideoDialog(context,exercisesBean.getExeVideoUrl());
                        videoDialog.show();
                    }
                });
            }
        }
    }

    private void addExercise() {

        if (exercisesListBean != null) {
            if (exercisesListBean.getCatId().equalsIgnoreCase("2") || exercisesListBean.getCatId().equalsIgnoreCase("4")) {
                int intHour=0,intMin=0,intSec=0;
                if (exercisesListBean.getExeHours() != null)
                    intHour = Integer.parseInt(exercisesListBean.getExeHours());
                if (exercisesListBean.getExeMin() != null)
                    intMin = Integer.parseInt(exercisesListBean.getExeMin());
                if (exercisesListBean.getExeSec() != null)
                    intSec = Integer.parseInt(exercisesListBean.getExeSec());

                setTimeDialog = new SetTimeDialog(context, intHour, intMin, intSec, new SetTimeDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strHour, String strMin, String strSec) {
                        if (preferences.getIsUpdatePlan() != null && !TextUtils.isEmpty(preferences.getIsUpdatePlan()))
                        {
                            if (preferences.getUpdateRoutineDataPref() != null) {
                                Gson gson = new Gson();
                                routinePlanDetailsBean = gson.fromJson(preferences.getUpdateRoutineDataPref(), new TypeToken<RoutinePlanDetailsBean>(){}.getType());
                            }
                            else
                                routinePlanDetailsBean = new RoutinePlanDetailsBean();

                            exercisesListBean.setExeHours(strHour);
                            exercisesListBean.setExeMin(strMin);
                            exercisesListBean.setExeSec(strSec);

                            if (strFrom != null && strFrom.equalsIgnoreCase("ExercisesActivity")) {
                                exxDetial.setUserId(preferences.getUserDataPref().getId().toString().trim());
                                exxDetial.setId(preferences.getIsUpdatePlan().trim());
                                exxDetial.setExes(exercisesListBean);
                                exxDetial.setExeHours(strHour);
                                exxDetial.setExeMin(strMin);
                                exxDetial.setExeSec(strSec);
                                routinePlanDetailsBean.getExxDetial().add(exxDetial);
                                preferences.saveUpdateRoutineData(routinePlanDetailsBean);
                            }
                            else {
                                for (int i=0;i<routinePlanDetailsBean.getExxDetial().size();i++) {
                                    if (exerciseId.equalsIgnoreCase(routinePlanDetailsBean.getExxDetial().get(i).getExes().getId())) {
                                        routinePlanDetailsBean.getExxDetial().get(i).setUserId(preferences.getUserDataPref().getId().toString().trim());
                                        routinePlanDetailsBean.getExxDetial().get(i).setId(preferences.getIsUpdatePlan().trim());
                                        routinePlanDetailsBean.getExxDetial().get(i).setExeHours(strHour);
                                        routinePlanDetailsBean.getExxDetial().get(i).setExeMin(strMin);
                                        routinePlanDetailsBean.getExxDetial().get(i).setExeSec(strSec);
                                        routinePlanDetailsBean.getExxDetial().get(i).setExes(exercisesListBean);
                                        preferences.saveUpdateRoutineData(routinePlanDetailsBean);
                                    }
                                }
                            }

                            Intent intent = new Intent(context, UpdateRoutineActivity.class);
                            startActivity(intent);
                        }
                        else {
                            if (preferences.getRoutineDataPref() != null) {
                                Gson gson = new Gson();
                                exercisesListBeans = gson.fromJson(preferences.getRoutineDataPref(), new TypeToken<List<ExercisesListBean>>(){}.getType());
                            }
                            else
                                exercisesListBeans = new ArrayList<>();

                            if (strFrom != null && strFrom.equalsIgnoreCase("ExercisesActivity")) {
                                exercisesListBean.setExeHours(strHour);
                                exercisesListBean.setExeMin(strMin);
                                exercisesListBean.setExeSec(strSec);
                                exercisesListBeans.add(exercisesListBean);
                                preferences.saveRoutineData(exercisesListBeans);
                            }
                            else {
                                for (int i=0;i<exercisesListBeans.size();i++) {
                                    if (exerciseId.equalsIgnoreCase(exercisesListBeans.get(i).getId())) {
                                        exercisesListBeans.get(i).setExeHours(strHour);
                                        exercisesListBeans.get(i).setExeMin(strMin);
                                        exercisesListBeans.get(i).setExeSec(strSec);
                                        preferences.saveRoutineData(exercisesListBeans);
                                    }
                                }
                            }

                            Intent intent = new Intent(context, CreateRoutineActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                setTimeDialog.show();
            }
            else {
                Intent intent = new Intent(context, SetsRepsDetailsActivity.class);
                intent.putExtra("From", strFrom);
                intent.putExtra("ExercisesListBean",exercisesListBean);
                startActivity(intent);
            }
        }
    }

    private void callExerciseDetailsApi(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            map.put("exe_id", id);

            Observable<ModelBean<ArrayList<ExerciseDetailsBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getExerciseDetailsAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ExerciseDetailsBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callExerciseDetailsApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ExerciseDetailsBean>> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                exercisesBean = apiExercisesBean.getResult().get(0);
                                setData();
                            }
                            else
                                CommonUtils.toast(context, ""+apiExercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callYogaExerciseDetailsApi(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            map.put("exe_id", id);

            Observable<ModelBean<ArrayList<YogaExerciseDetailsBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getYogaExerciseDetailsAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<YogaExerciseDetailsBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callExerciseDetailsApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<YogaExerciseDetailsBean>> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                yogaExerciseDetailsBean = apiExercisesBean.getResult().get(0);
                                setData();
                            }
                            else
                                CommonUtils.toast(context, ""+apiExercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callCrossFitExerciseDetailsApi(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            map.put("exe_id", id);

            Observable<ModelBean<ArrayList<YogaExerciseDetailsBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCrossFitExerciseDetailsAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<YogaExerciseDetailsBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callExerciseDetailsApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<YogaExerciseDetailsBean>> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                yogaExerciseDetailsBean = apiExercisesBean.getResult().get(0);
                                setData();
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
