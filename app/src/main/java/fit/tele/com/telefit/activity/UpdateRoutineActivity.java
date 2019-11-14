package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExercisesDragNDropAdapter;
import fit.tele.com.telefit.adapter.ExercisesUpdateDragNDropAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCreateRoutineBinding;
import fit.tele.com.telefit.helper.OnStartDragListener;
import fit.tele.com.telefit.helper.SimpleItemTouchHelperCallback;
import fit.tele.com.telefit.modelBean.CreatePlanApiBean;
import fit.tele.com.telefit.modelBean.ExeDetl;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.UpdatePlanApiBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateRoutineActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener {

    ActivityCreateRoutineBinding binding;
    private ExercisesUpdateDragNDropAdapter exercisesDragNDropAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private UpdatePlanApiBean createPlanApiBean;
    private RoutinePlanDetailsBean exercisesListBeans;
    private String dayFlag = "1", planId = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_create_routine;
    }

    @Override
    public void init() {
        binding = (ActivityCreateRoutineBinding) getBindingObj();
        binding.txtAdd.setText("Update");
        createPlanApiBean = new UpdatePlanApiBean();
        setListner();
    }

    private void setListner() {
        binding.txtHeaderName.setText("Update Routine");
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.cleanUpdateRoutinedata();
                preferences.cleanUpdatePlan();
                onBackPressed();
            }
        });
        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llAddPlan.setOnClickListener(this);
        binding.txtAdd.setOnClickListener(this);
        binding.txtRoutineDate.setOnClickListener(this);

        ArrayAdapter<String> spinnerRoutineTypeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.routine_type));
        spinnerRoutineTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiType.setAdapter(spinnerRoutineTypeArrayAdapter);

        ArrayAdapter<String> spinnerDifficultyLevelArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.difficulty_level));
        spinnerDifficultyLevelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiLevel.setAdapter(spinnerDifficultyLevelArrayAdapter);

        ArrayAdapter<String> spinnerDayArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.days));
        spinnerDayArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiDay.setAdapter(spinnerDayArrayAdapter);

        binding.spiDay.setEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);
        binding.rvExercises.setHasFixedSize(true);
        if (exercisesDragNDropAdapter == null) {
            exercisesDragNDropAdapter = new ExercisesUpdateDragNDropAdapter(context, this, new ExercisesUpdateDragNDropAdapter.ClickListener() {
                @Override
                public void onClick(String exe_id, ExxDetial exxDetial) {
                    Intent intent = new Intent(context, ExerciseDetailsActivity.class);
                    intent.putExtra("ExerciseDetails", exe_id);
                    intent.putExtra("ExercisesListBean", exxDetial.getExes());
                    context.startActivity(intent);
                }
            });
        }
        binding.rvExercises.setAdapter(exercisesDragNDropAdapter);
        exercisesDragNDropAdapter.clearAll();
        if (preferences.getUpdateRoutineDataPref() != null && !TextUtils.isEmpty(preferences.getUpdateRoutineDataPref()))
        {
            Gson gson = new Gson();
            exercisesListBeans = gson.fromJson(preferences.getUpdateRoutineDataPref(), new TypeToken<RoutinePlanDetailsBean>(){}.getType());
            setData(exercisesListBeans);
        }
        else {
            if (preferences.getIsUpdatePlan() != null && !TextUtils.isEmpty(preferences.getIsUpdatePlan()))
            {
                planId = preferences.getIsUpdatePlan();
                callRoutinePlanDetailsApi(planId);
            }
        }

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(exercisesDragNDropAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rvExercises);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
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

            case R.id.ll_add_plan:
                exercisesListBeans.setExxDetial(exercisesDragNDropAdapter.getAllData());
                preferences.saveUpdateRoutineData(exercisesListBeans);
                intent = new Intent(context, MainExercisesActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                    if (TextUtils.isEmpty(binding.inputRoutineName.getText().toString()))
                        CommonUtils.toast(context, "Please enter Routine Name");
                    else {
                        createPlanApiBean.setRoutineName(binding.inputRoutineName.getText().toString().trim());
                        createPlanApiBean.setDayOfTheWeek(binding.spiDay.getSelectedItem().toString());
                        createPlanApiBean.setRoutineType(binding.spiType.getSelectedItem().toString());
                        createPlanApiBean.setDifficultyLevel(binding.spiLevel.getSelectedItem().toString());
                        createPlanApiBean.setDayFlag(getDayFlag());
                        createPlanApiBean.setRoutinePId(preferences.getIsUpdatePlan().toString());
                        ExeDetl exeDetl;
                        ArrayList<ExeDetl> exeDetls;
                        boolean isAllGood = true;
                        if (exercisesDragNDropAdapter != null) {
                            if (exercisesDragNDropAdapter.getAllData().size() > 0) {
                                exeDetls = new ArrayList<>();
                                for (int i=0;i<exercisesDragNDropAdapter.getAllData().size();i++)
                                {
                                    exeDetl = new ExeDetl();
                                    if ((exercisesDragNDropAdapter.getAllData().get(i).getSetsReps() == null || exercisesDragNDropAdapter.getAllData().get(i).getSetsReps().size() < 1) &&
                                            (exercisesDragNDropAdapter.getAllData().get(i).getExeHours() == null || exercisesDragNDropAdapter.getAllData().get(i).getExeHours() == ""))
                                    {
                                        CommonUtils.toast(context, "Please set exercise detail for "+exercisesDragNDropAdapter.getAllData().get(i).getExes().getExeTitle());
                                        isAllGood = false;
                                        return;
                                    }
                                    else {
                                        exeDetl.setExeid(exercisesDragNDropAdapter.getAllData().get(i).getExes().getId());
                                        if (exercisesDragNDropAdapter.getAllData().get(i).getExeHours() != null && !TextUtils.isEmpty(exercisesDragNDropAdapter.getAllData().get(i).getExeHours()))
                                            exeDetl.setExeHours(exercisesDragNDropAdapter.getAllData().get(i).getExeHours());
                                        else
                                            exeDetl.setExeHours("");
                                        if (exercisesDragNDropAdapter.getAllData().get(i).getExeMin() != null && !TextUtils.isEmpty(exercisesDragNDropAdapter.getAllData().get(i).getExeMin()))
                                            exeDetl.setExeMin(exercisesDragNDropAdapter.getAllData().get(i).getExeMin());
                                        else
                                            exeDetl.setExeMin("");
                                        if (exercisesDragNDropAdapter.getAllData().get(i).getExeSec() != null && !TextUtils.isEmpty(exercisesDragNDropAdapter.getAllData().get(i).getExeSec()))
                                            exeDetl.setExeSec(exercisesDragNDropAdapter.getAllData().get(i).getExeSec());
                                        else
                                            exeDetl.setExeSec("");
                                        if (exercisesDragNDropAdapter.getAllData().get(i).getSetsReps() != null && exercisesDragNDropAdapter.getAllData().get(i).getSetsReps().size() > 0)
                                            exeDetl.setSetsRepsBeans(exercisesDragNDropAdapter.getAllData().get(i).getSetsReps());
                                        else
                                            exeDetl.setSetsRepsBeans(null);
                                        exeDetl.setCatid(exercisesDragNDropAdapter.getAllData().get(i).getExes().getCatId());
                                        exeDetl.setRoutineExeOrder(""+(i+1));
                                        exeDetls.add(exeDetl);
                                    }
                                }

                                if (isAllGood && exeDetls.size() > 0)
                                {
                                    createPlanApiBean.setExeId(exeDetls);
                                    callUpdateApiApi();
                                }
                                else
                                    CommonUtils.toast(context, "Please add exercise!");
                            }
                            else
                                CommonUtils.toast(context, "Please add exercise!");
                        }
                    }
                break;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void callUpdateApiApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ExercisesListBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).updateRoutineApi(createPlanApiBean);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ExercisesListBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCreateApiApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ExercisesListBean> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getStatus() == 1) {
                                preferences.cleanUpdateRoutinedata();
                                preferences.cleanUpdatePlan();
                                Intent intent = new Intent(context, FitnessActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private String getDayFlag() {
        if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 1, Monday"))
            return "1";
        else if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 2, Tuesday"))
            return "2";
        else if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 3, Wednesday"))
            return "3";
        else if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 4, Thursday"))
            return "4";
        else if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 5, Friday"))
            return "5";
        else if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 6, Saturday"))
            return "6";
        else if (binding.spiDay.getSelectedItem().toString().equalsIgnoreCase("Day 7, Sunday"))
            return "7";
        else
            return "1";
    }

    private void setData(RoutinePlanDetailsBean data) {
        if (data != null) {

            if (data.getRoutineName() != null && !TextUtils.isEmpty(data.getRoutineName()))
                binding.inputRoutineName.setText(data.getRoutineName());
            if (data.getDifficultyLevel() != null && !TextUtils.isEmpty(data.getDifficultyLevel()))
            {
                if (data.getDifficultyLevel().equalsIgnoreCase("Beginner"))
                    binding.spiLevel.setSelection(0);
                if (data.getDifficultyLevel().equalsIgnoreCase("Intermediate"))
                    binding.spiLevel.setSelection(1);
                if (data.getDifficultyLevel().equalsIgnoreCase("Advanced"))
                    binding.spiLevel.setSelection(2);
            }
            if (data.getRoutineType() != null && !TextUtils.isEmpty(data.getRoutineType()))
            {
                if (data.getRoutineType().equalsIgnoreCase("General Health"))
                    binding.spiType.setSelection(0);
                if (data.getRoutineType().equalsIgnoreCase("Cutting"))
                    binding.spiType.setSelection(1);
                if (data.getRoutineType().equalsIgnoreCase("Bulking"))
                    binding.spiType.setSelection(2);
                if (data.getRoutineType().equalsIgnoreCase("Sport Specific"))
                    binding.spiType.setSelection(3);
            }
            if (data.getDayFlag() != null && !TextUtils.isEmpty(data.getDayFlag()))
            {
                if (data.getDayFlag().equalsIgnoreCase("1"))
                    binding.spiDay.setSelection(0);
                if (data.getDayFlag().equalsIgnoreCase("2"))
                    binding.spiDay.setSelection(1);
                if (data.getDayFlag().equalsIgnoreCase("3"))
                    binding.spiDay.setSelection(2);
                if (data.getDayFlag().equalsIgnoreCase("4"))
                    binding.spiDay.setSelection(3);
                if (data.getDayFlag().equalsIgnoreCase("5"))
                    binding.spiDay.setSelection(4);
                if (data.getDayFlag().equalsIgnoreCase("6"))
                    binding.spiDay.setSelection(5);
                if (data.getDayFlag().equalsIgnoreCase("7"))
                    binding.spiDay.setSelection(6);
            }
            if (data.getExxDetial() != null && data.getExxDetial().size() > 0)
            {
                for (int i=0;i<data.getExxDetial().size();i++)
                {
                    data.getExxDetial().get(i).getExes().setExeHours(data.getExxDetial().get(i).getExeHours());
                    data.getExxDetial().get(i).getExes().setExeMin(data.getExxDetial().get(i).getExeMin());
                    data.getExxDetial().get(i).getExes().setExeSec(data.getExxDetial().get(i).getExeSec());
                    data.getExxDetial().get(i).getExes().setSetsRepsBeans(data.getExxDetial().get(i).getSetsReps());
                }
            }

            exercisesDragNDropAdapter.addAllList(data.getExxDetial());
        }
    }

    private void callRoutinePlanDetailsApi(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("routine_p_id", id);

            Observable<ModelBean<ArrayList<RoutinePlanDetailsBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getRoutinePlanDetailsAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<RoutinePlanDetailsBean>>>() {
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
                        public void onNext(ModelBean<ArrayList<RoutinePlanDetailsBean>> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                if (apiExercisesBean.getResult().get(0).getExxDetial().size() > 0)
                                {
                                    setData(apiExercisesBean.getResult().get(0));
                                    exercisesListBeans = apiExercisesBean.getResult().get(0);
                                    preferences.saveUpdateRoutineData(apiExercisesBean.getResult().get(0));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preferences.cleanUpdateRoutinedata();
        preferences.cleanUpdatePlan();
        Intent intent = new Intent(UpdateRoutineActivity.this, FitnessActivity.class);
        startActivity(intent);
    }
}
