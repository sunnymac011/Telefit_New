package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
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
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityExerciseDetailsBinding;
import fit.tele.com.telefit.dialog.SetsRepsDialog;
import fit.tele.com.telefit.dialog.VideoDialog;
import fit.tele.com.telefit.modelBean.ExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.ExercisesBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ModelBean;
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
    private SetsRepsDialog setsRepsDialog;

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
        binding.llFitness.setOnClickListener(this);
        binding.txtAdd.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null) {
            if (intent.hasExtra("ExerciseDetails"))
                exerciseId = intent.getStringExtra("ExerciseDetails");
            if (intent.hasExtra("from"))
                strFrom = intent.getStringExtra("from");
            if(getIntent() != null && getIntent().hasExtra("ExercisesListBean"))
                exercisesListBean = intent.getParcelableExtra("ExercisesListBean");
        }

        if (strFrom.equalsIgnoreCase("gym") || strFrom.equalsIgnoreCase("hiit"))
            callExerciseDetailsApi(exerciseId);
        else if (strFrom.equalsIgnoreCase("yoga"))
            callYogaExerciseDetailsApi(exerciseId);
        else if (strFrom.equalsIgnoreCase("crossfit"))
            callCrossFitExerciseDetailsApi(exerciseId);

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

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                setsRepsDialog = new SetsRepsDialog(context, new SetsRepsDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strSets, String strReps, String strTime) {
                        if (preferences.getRoutineDataPref() != null) {
                            Gson gson = new Gson();
                            exercisesListBeans = gson.fromJson(preferences.getRoutineDataPref(), new TypeToken<List<ExercisesListBean>>(){}.getType());
                        }
                        else
                            exercisesListBeans = new ArrayList<>();

                        exercisesListBean.setSets(strSets);
                        exercisesListBean.setReps(strReps);
                        exercisesListBean.setTime_between_sets(strTime);
                        exercisesListBeans.add(exercisesListBean);
                        preferences.saveRoutineData(exercisesListBeans);

                        Intent intent = new Intent(context, CreateRoutineActivity.class);
                        startActivity(intent);
                    }
                });
                setsRepsDialog.show();
                break;
        }
    }

    private void setData() {
        if(exercisesBean != null || yogaExerciseDetailsBean != null) {
            binding.txtExerciseDetails.setMovementMethod(new ScrollingMovementMethod());
            if (strFrom.equalsIgnoreCase("yoga")) {
                binding.rvVideo.setVisibility(View.GONE);
                binding.llMuscle.setVisibility(View.GONE);
                binding.txtEquipment.setText("Category");
                if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getOptArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName()))
                    binding.txtEquipmentType.setText(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName());
                if (yogaExerciseDetailsBean.getExeTitle() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeTitle()))
                    binding.txtExerciseType.setText(yogaExerciseDetailsBean.getCatName());

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
            else if (strFrom.equalsIgnoreCase("crossfit")) {
                binding.rvVideo.setVisibility(View.VISIBLE);
                binding.llMuscle.setVisibility(View.GONE);
                binding.txtEquipment.setText("Category");
                if (yogaExerciseDetailsBean != null && yogaExerciseDetailsBean.getOptArray() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName()))
                    binding.txtEquipmentType.setText(yogaExerciseDetailsBean.getOptArray().get(0).getSubCatName());
                if (yogaExerciseDetailsBean.getExeTitle() != null && !TextUtils.isEmpty(yogaExerciseDetailsBean.getExeTitle()))
                    binding.txtExerciseType.setText(yogaExerciseDetailsBean.getCatName());

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
                binding.rvVideo.setVisibility(View.GONE);
                binding.llMuscle.setVisibility(View.VISIBLE);
                binding.txtEquipment.setText("Equipment");
                if (exercisesBean.getSubArray().get(0).getSubCatOption() != null && !TextUtils.isEmpty(exercisesBean.getSubArray().get(0).getSubCatOption()))
                    binding.txtMuscleType.setText(exercisesBean.getSubArray().get(0).getSubCatOption());
                if (exercisesBean.getSubArray().get(1).getSubCatOption() != null && !TextUtils.isEmpty(exercisesBean.getSubArray().get(1).getSubCatOption()))
                    binding.txtExerciseType.setText(exercisesBean.getSubArray().get(1).getSubCatOption());
                if (exercisesBean.getSubArray().get(2).getSubCatOption() != null && !TextUtils.isEmpty(exercisesBean.getSubArray().get(2).getSubCatOption()))
                    binding.txtEquipmentType.setText(exercisesBean.getSubArray().get(2).getSubCatOption());

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
