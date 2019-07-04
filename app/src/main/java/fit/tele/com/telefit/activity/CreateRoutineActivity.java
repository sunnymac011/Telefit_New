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

    @Override
    public int getLayoutResId() {
        return R.layout.activity_create_routine;
    }

    @Override
    public void init() {
        binding = (ActivityCreateRoutineBinding) getBindingObj();
        createPlanApiBean = new CreatePlanApiBean();
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);
        binding.rvExercises.setHasFixedSize(true);
        if (exercisesDragNDropAdapter == null) {
            exercisesDragNDropAdapter = new ExercisesDragNDropAdapter(context, this);
        }
        binding.rvExercises.setAdapter(exercisesDragNDropAdapter);
        exercisesDragNDropAdapter.clearAll();
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

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_add_plan:
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
                    else if (TextUtils.isEmpty(binding.txtRoutineDate.getText().toString()))
                        CommonUtils.toast(context, "Please enter Routine Date");
                    else {
                        createPlanApiBean.setRoutineName(binding.inputRoutineName.getText().toString().trim());
                        createPlanApiBean.setDayOfTheWeek(binding.txtWeekDay.getText().toString());
                        createPlanApiBean.setRoutineType(binding.spiType.getSelectedItem().toString());
                        createPlanApiBean.setDifficultyLevel(binding.spiLevel.getSelectedItem().toString());
                        createPlanApiBean.setDayFlag(dayFlag);
                        createPlanApiBean.setRoutineDate(binding.txtRoutineDate.getText().toString().trim());
                        ExeDetl exeDetl;
                        ArrayList<ExeDetl> exeDetls;
                        if (exercisesDragNDropAdapter != null) {
                            if (exercisesDragNDropAdapter.getAllData().size() > 0) {
                                exeDetls = new ArrayList<>();
                                for (int i=0;i<exercisesDragNDropAdapter.getAllData().size();i++)
                                {
                                    exeDetl = new ExeDetl();
                                    exeDetl.setExeid(exercisesDragNDropAdapter.getAllData().get(i).getId());
                                    exeDetl.setSets(exercisesDragNDropAdapter.getAllData().get(i).getSets());
                                    exeDetl.setReps(exercisesDragNDropAdapter.getAllData().get(i).getReps());
                                    exeDetl.setTimebetweenreps(exercisesDragNDropAdapter.getAllData().get(i).getTime_between_sets());
                                    exeDetls.add(exeDetl);
                                }

                                if (exeDetls.size() > 0)
                                {
                                    createPlanApiBean.setExeId(exeDetls);
                                    callCreateApiApi();
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
                                Intent intent = new Intent(context, FitnessActivity.class);
                                startActivity(intent);
                                CreateRoutineActivity.this.overridePendingTransition(0, 0);
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
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
    }
}
