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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExercisesDragNDropAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCreateRoutineBinding;
import fit.tele.com.telefit.helper.OnStartDragListener;
import fit.tele.com.telefit.helper.SimpleItemTouchHelperCallback;
import fit.tele.com.telefit.modelBean.CreatePlanApiBean;
import fit.tele.com.telefit.modelBean.ExeDetl;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RoutinePrefBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateRoutineActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener, DatePickerDialog.OnDateSetListener {

    ActivityCreateRoutineBinding binding;
    private ExercisesDragNDropAdapter exercisesDragNDropAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<ExercisesListBean> exercisesListBeans = new ArrayList<>();
    private CreatePlanApiBean createPlanApiBean;
    private DatePickerDialog dpd;
    private String dayFlag = "1";
    private RoutinePrefBean routinePrefBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_create_routine;
    }

    @Override
    public void init() {
        binding = (ActivityCreateRoutineBinding) getBindingObj();
        createPlanApiBean = new CreatePlanApiBean();
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);
        binding.rvExercises.setHasFixedSize(true);
        if (exercisesDragNDropAdapter == null) {
            exercisesDragNDropAdapter = new ExercisesDragNDropAdapter(context, this, new ExercisesDragNDropAdapter.ClickListener() {
                @Override
                public void onClick(String exe_id, ExercisesListBean exercisesListBean) {

                    routinePrefBean = new RoutinePrefBean();

                    routinePrefBean.setRoutineName(binding.inputRoutineName.getText().toString());
                    routinePrefBean.setDayOfTheWeek(binding.spiDay.getSelectedItem().toString());
                    routinePrefBean.setRoutineType(binding.spiType.getSelectedItem().toString());
                    routinePrefBean.setDifficultyLevel(binding.spiLevel.getSelectedItem().toString());
                    routinePrefBean.setDayFlag(getDayFlag());

                    preferences.saveRoutinHeadereData(routinePrefBean);

                    Intent intent = new Intent(context, ExerciseDetailsActivity.class);
                    intent.putExtra("ExerciseDetails", exe_id);
                    intent.putExtra("ExercisesListBean", exercisesListBean);
                    startActivity(intent);
                }
            });
        }
        binding.rvExercises.setAdapter(exercisesDragNDropAdapter);
        exercisesDragNDropAdapter.clearAll();

        if (preferences.getRoutineHeaderDataPref() != null) {
            Gson gson = new Gson();
            routinePrefBean = gson.fromJson(preferences.getRoutineHeaderDataPref(), new TypeToken<RoutinePrefBean>(){}.getType());

            if (routinePrefBean != null) {
                if (routinePrefBean.getRoutineName() != null && !TextUtils.isEmpty(routinePrefBean.getRoutineName()))
                    binding.inputRoutineName.setText(routinePrefBean.getRoutineName());
                if (routinePrefBean.getDifficultyLevel() != null && !TextUtils.isEmpty(routinePrefBean.getDifficultyLevel()))
                {
                    if (routinePrefBean.getDifficultyLevel().equalsIgnoreCase("Beginner"))
                        binding.spiLevel.setSelection(0);
                    if (routinePrefBean.getDifficultyLevel().equalsIgnoreCase("Intermediate"))
                        binding.spiLevel.setSelection(1);
                    if (routinePrefBean.getDifficultyLevel().equalsIgnoreCase("Advanced"))
                        binding.spiLevel.setSelection(2);
                }
                if (routinePrefBean.getRoutineType() != null && !TextUtils.isEmpty(routinePrefBean.getRoutineType()))
                {
                    if (routinePrefBean.getRoutineType().equalsIgnoreCase("General Health"))
                        binding.spiType.setSelection(0);
                    if (routinePrefBean.getRoutineType().equalsIgnoreCase("Cutting"))
                        binding.spiType.setSelection(1);
                    if (routinePrefBean.getRoutineType().equalsIgnoreCase("Bulking"))
                        binding.spiType.setSelection(2);
                    if (routinePrefBean.getRoutineType().equalsIgnoreCase("Sport Specific"))
                        binding.spiType.setSelection(3);
                }
                if (routinePrefBean.getDayFlag() != null && !TextUtils.isEmpty(routinePrefBean.getDayFlag()))
                {
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("1"))
                        binding.spiDay.setSelection(0);
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("2"))
                        binding.spiDay.setSelection(1);
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("3"))
                        binding.spiDay.setSelection(2);
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("4"))
                        binding.spiDay.setSelection(3);
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("5"))
                        binding.spiDay.setSelection(4);
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("6"))
                        binding.spiDay.setSelection(5);
                    if (routinePrefBean.getDayFlag().equalsIgnoreCase("7"))
                        binding.spiDay.setSelection(6);
                }
            }
        }

        if (preferences.getRoutineDataPref() != null) {
            Gson gson = new Gson();
            exercisesListBeans = gson.fromJson(preferences.getRoutineDataPref(), new TypeToken<ArrayList<ExercisesListBean>>(){}.getType());
            exercisesDragNDropAdapter.addAllList(exercisesListBeans);
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
//                if (exercisesDragNDropAdapter != null && exercisesDragNDropAdapter.getAllData().size() > 0) {
//                    preferences.cleanRoutinedata();
//                    preferences.saveRoutineData(exercisesDragNDropAdapter.getAllData());
//                }

                routinePrefBean = new RoutinePrefBean();

                routinePrefBean.setRoutineName(binding.inputRoutineName.getText().toString());
                routinePrefBean.setDayOfTheWeek(binding.spiDay.getSelectedItem().toString());
                routinePrefBean.setRoutineType(binding.spiType.getSelectedItem().toString());
                routinePrefBean.setDifficultyLevel(binding.spiLevel.getSelectedItem().toString());
                routinePrefBean.setDayFlag(getDayFlag());

                preferences.saveRoutinHeadereData(routinePrefBean);
                preferences.saveRoutineData(exercisesDragNDropAdapter.getAllData());

                intent = new Intent(context, MainExercisesActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_routine_date:
                datePicker();
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
                        ExeDetl exeDetl;
                        ArrayList<ExeDetl> exeDetls;
                        boolean isAllGood = true;
                        if (exercisesDragNDropAdapter != null) {
                            if (exercisesDragNDropAdapter.getAllData().size() > 0) {
                                exeDetls = new ArrayList<>();
                                for (int i=0;i<exercisesDragNDropAdapter.getAllData().size();i++)
                                {
                                    if ((exercisesDragNDropAdapter.getAllData().get(i).getSetsRepsBeans() == null || exercisesDragNDropAdapter.getAllData().get(i).getSetsRepsBeans().size() < 1) &&
                                            (exercisesDragNDropAdapter.getAllData().get(i).getExeHours() == null || exercisesDragNDropAdapter.getAllData().get(i).getExeHours() == ""))
                                    {
                                        CommonUtils.toast(context, "Please set exercise detail for "+exercisesDragNDropAdapter.getAllData().get(i).getExeTitle());
                                        isAllGood = false;
                                        return;
                                    }
                                    else {
                                        exeDetl = new ExeDetl();
                                        exeDetl.setExeid(exercisesDragNDropAdapter.getAllData().get(i).getId());
                                        exeDetl.setExeHours(exercisesDragNDropAdapter.getAllData().get(i).getExeHours());
                                        exeDetl.setExeMin(exercisesDragNDropAdapter.getAllData().get(i).getExeMin());
                                        exeDetl.setExeSec(exercisesDragNDropAdapter.getAllData().get(i).getExeSec());
                                        exeDetl.setSetsRepsBeans(exercisesDragNDropAdapter.getAllData().get(i).getSetsRepsBeans());
                                        exeDetl.setCatid(exercisesDragNDropAdapter.getAllData().get(i).getCatId());
                                        exeDetl.setRoutineExeOrder(""+(i+1));
                                        exeDetls.add(exeDetl);
                                    }
                                }

                                if (isAllGood && exeDetls.size() > 0)
                                {
                                    createPlanApiBean.setExeId(exeDetls);
                                    callCreateApiApi();
                                }
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

    private void callCreateApiApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ExercisesListBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).createRoutineApi(createPlanApiBean);
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
                                preferences.cleanRoutinedata();
                                preferences.cleanRoutineHeaderedata();
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

    private void datePicker() {
        if (dpd == null || !dpd.isVisible()) {
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int mm = now.get(Calendar.MONTH);
            int dd = now.get(Calendar.DAY_OF_MONTH);


            if(binding.txtRoutineDate != null && !binding.txtRoutineDate.getText().toString().equalsIgnoreCase("")) {
                String[] date = binding.txtRoutineDate.getText().toString().split("/");
                if(date.length == 3) {
                    dd = Integer.parseInt(date[1]);
                    mm = Integer.parseInt(date[0]) - 1;
                    year = Integer.parseInt(date[2]);
                }
            }

            dpd = DatePickerDialog.newInstance(CreateRoutineActivity.this, year, mm, dd);
            dpd.setThemeDark(false);
            dpd.vibrate(false);
            dpd.dismissOnPause(true);
            dpd.setMinDate(now);
            dpd.showYearPickerFirst(false);
            dpd.setVersion(DatePickerDialog.Version.VERSION_1);
            dpd.setAccentColor(ContextCompat.getColor(context, R.color.colorAccent));
            dpd.setTitle("Select date");

            dpd.show(getFragmentManager(), "Datepickerdialog");
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date =  dayOfMonth + "/" + ((monthOfYear+1) > 9 ? (monthOfYear+1) : ("0"+(monthOfYear+1))) + "/" + year;
        binding.txtRoutineDate.setText(date);

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = inFormat.parse(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date1);
            if (goal.equalsIgnoreCase("Monday"))
            {
                binding.txtWeekDay.setText("Day 1, Monday");
                dayFlag = "1";
            }
            if (goal.equalsIgnoreCase("Tuesday"))
            {
                binding.txtWeekDay.setText("Day 2, Tuesday");
                dayFlag = "2";
            }
            if (goal.equalsIgnoreCase("Wednesday"))
            {
                binding.txtWeekDay.setText("Day 3, Wednesday");
                dayFlag = "3";
            }
            if (goal.equalsIgnoreCase("Thursday"))
            {
                binding.txtWeekDay.setText("Day 4, Thursday");
                dayFlag = "4";
            }
            if (goal.equalsIgnoreCase("Friday"))
            {
                binding.txtWeekDay.setText("Day 5, Friday");
                dayFlag = "5";
            }
            if (goal.equalsIgnoreCase("Saturday"))
            {
                binding.txtWeekDay.setText("Day 6, Saturday");
                dayFlag = "6";
            }
            if (goal.equalsIgnoreCase("Sunday"))
            {
                binding.txtWeekDay.setText("Day 7, Sunday");
                dayFlag = "7";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setListner();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
    }

    @Override
    public void onBackPressed() {
        preferences.cleanRoutinedata();
        preferences.cleanRoutineHeaderedata();
        Intent intent = new Intent(CreateRoutineActivity.this,FitnessActivity.class);
        startActivity(intent);
    }
}
