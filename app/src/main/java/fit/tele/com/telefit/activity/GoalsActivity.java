package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.GoalCategoryAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityGoalsBinding;
import fit.tele.com.telefit.modelBean.CaloriesBarBean;
import fit.tele.com.telefit.modelBean.GoalBarBean;
import fit.tele.com.telefit.modelBean.GoalBean;
import fit.tele.com.telefit.modelBean.GoalDataBean;
import fit.tele.com.telefit.modelBean.GoalDetailsBean;
import fit.tele.com.telefit.modelBean.MealCategoryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoalsActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ActivityGoalsBinding binding;
    private DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    private GoalCategoryAdapter goalCategoryAdapter;
    private ArrayList<String> goalList = new ArrayList<>();
    private GoalBarBean goalBarBean;
    private DatePickerDialog dpd;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_goals;
    }

    @Override
    public void init() {
        binding = (ActivityGoalsBinding) getBindingObj();
        setListner();
    }

    private void setListner() {
        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.txtGoal.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();

        goalList.add("Weight");
        goalList.add("Protein");
        goalList.add("Carbs");
        goalList.add("Fat");
        goalList.add("Body Fat");
        goalList.add("Cholesterol");
        goalList.add("Fiber");
        goalList.add("Water");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);
        goalCategoryAdapter = new GoalCategoryAdapter(context, new GoalCategoryAdapter.ClickListener() {
            @Override
            public void onClick(int pos, String goalName) {
                setLineChartData(goalName);
            }
        });

        binding.rvExercises.setAdapter(goalCategoryAdapter);
        goalCategoryAdapter.clearAll();
        goalCategoryAdapter.addAllList(goalList);

        binding.txtGoalDate.setOnClickListener(this);
        if (preferences.getGoalDatePref().equalsIgnoreCase("0"))
        {
            binding.txtGoalDate.setText(format1.format(calendar.getTime()));
            callGetGoalApi(format1.format(calendar.getTime()));
        }
        else
        {
            binding.txtGoalDate.setText(preferences.getGoalDatePref());
            callGetGoalApi(preferences.getGoalDatePref());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_nutrition:
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.ll_profile:
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.txt_goal:
                intent = new Intent(context, EditGoalsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.txt_goal_date:
                datePicker();
                break;
        }
    }

    private void callGetGoalApi(String strDate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("goal_date", strDate);
            map.put("is_racipe_meal", "2");

            Observable<ModelBean<GoalBarBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getGoalApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<GoalBarBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetGoalApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<GoalBarBean> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                goalBarBean = apiFoodBean.getResult();
                                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                                {
                                    if (goalBarBean.getGoalWeight() != null) {
                                        float weightGoal = Float.parseFloat(goalBarBean.getGoalWeight());
                                        setCaloriesBarChart(weightGoal);
                                    }
                                    GoalDataBean goalDataBean = new GoalDataBean();
                                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getWeight() != null)
                                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getWeight()));
                                    else
                                        goalDataBean.setMonday(0f);
                                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getWeight() != null)
                                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getWeight()));
                                    else
                                        goalDataBean.setTuesday(0f);
                                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getWeight() != null)
                                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getWeight()));
                                    else
                                        goalDataBean.setWednesday(0f);
                                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getWeight() != null)
                                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getWeight()));
                                    else
                                        goalDataBean.setThursday(0f);
                                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getWeight() != null)
                                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getWeight()));
                                    else
                                        goalDataBean.setFriday(0f);
                                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getWeight() != null)
                                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getWeight()));
                                    else
                                        goalDataBean.setSaturday(0f);
                                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getWeight() != null)
                                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getWeight()));
                                    else
                                        goalDataBean.setSunday(0f);

                                    addCaloriesBarDataSet(goalDataBean);
                                }
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void setLineChartData(String selectedNutrition){
        if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
        {
            if (selectedNutrition.equalsIgnoreCase("Protein")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getProtein() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getProtein());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getMealProtein() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getMealProtein()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getMealProtein() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getMealProtein()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getMealProtein() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getMealProtein()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getMealProtein() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getMealProtein()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getMealProtein() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getMealProtein()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getMealProtein() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getMealProtein()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getMealProtein() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getMealProtein()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Carbs")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getCarbs() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getCarbs());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getMealCarbs() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getMealCarbs()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getMealCarbs() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getMealCarbs()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getMealCarbs() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getMealCarbs()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getMealCarbs() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getMealCarbs()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getMealCarbs() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getMealCarbs()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getMealCarbs() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getMealCarbs()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getMealCarbs() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getMealCarbs()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Fat")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getFat() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getFat());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getMealFat() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getMealFat()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getMealFat() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getMealFat()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getMealFat() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getMealFat()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getMealFat() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getMealFat()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getMealFat() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getMealFat()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getMealFat() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getMealFat()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getMealFat() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getMealFat()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Cholesterol")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getCholesterol() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getCholesterol());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getMealCholesterol() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getMealCholesterol()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getMealCholesterol() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getMealCholesterol()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getMealCholesterol() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getMealCholesterol()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getMealCholesterol() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getMealCholesterol()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getMealCholesterol() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getMealCholesterol()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getMealCholesterol() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getMealCholesterol()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getMealCholesterol() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getMealCholesterol()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Fiber")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getFiber() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getFiber());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getMealFiber() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getMealFiber()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getMealFiber() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getMealFiber()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getMealFiber() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getMealFiber()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getMealFiber() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getMealFiber()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getMealFiber() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getMealFiber()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getMealFiber() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getMealFiber()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getMealFiber() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getMealFiber()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Weight")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getGoalWeight() != null) {
                        float weightGoal = Float.parseFloat(goalBarBean.getGoalWeight());
                        setCaloriesBarChart(weightGoal);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getWeight() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getWeight()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getWeight() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getWeight()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getWeight() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getWeight()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getWeight() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getWeight()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getWeight() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getWeight()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getWeight() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getWeight()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getWeight() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getWeight()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Body Fat")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getGoalBodyFat() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getGoalBodyFat());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getBodyFat() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getBodyFat()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getBodyFat() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getBodyFat()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getBodyFat() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getBodyFat()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getBodyFat() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getBodyFat()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getBodyFat() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getBodyFat()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getBodyFat() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getBodyFat()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getBodyFat() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getBodyFat()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Water")) {
                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails() != null)
                {
                    if (goalBarBean.getGoalWater() != null) {
                        float goalValue = Float.parseFloat(goalBarBean.getGoalWater());
                        setCaloriesBarChart(goalValue);
                    }
                    GoalDataBean goalDataBean = new GoalDataBean();
                    if (goalBarBean.getGoalDetails().getGoalMonday() != null && goalBarBean.getGoalDetails().getGoalMonday().getWater() != null)
                        goalDataBean.setMonday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalMonday().getWater()));
                    else
                        goalDataBean.setMonday(0f);
                    if (goalBarBean.getGoalDetails().getGoalTuesday() != null && goalBarBean.getGoalDetails().getGoalTuesday().getWater() != null)
                        goalDataBean.setTuesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalTuesday().getWater()));
                    else
                        goalDataBean.setTuesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalWednesday() != null && goalBarBean.getGoalDetails().getGoalWednesday().getWater() != null)
                        goalDataBean.setWednesday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalWednesday().getWater()));
                    else
                        goalDataBean.setWednesday(0f);
                    if (goalBarBean.getGoalDetails().getGoalThursday() != null && goalBarBean.getGoalDetails().getGoalThursday().getWater() != null)
                        goalDataBean.setThursday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalThursday().getWater()));
                    else
                        goalDataBean.setThursday(0f);
                    if (goalBarBean.getGoalDetails().getGoalFriday() != null && goalBarBean.getGoalDetails().getGoalFriday().getWater() != null)
                        goalDataBean.setFriday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalFriday().getWater()));
                    else
                        goalDataBean.setFriday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSaturday() != null && goalBarBean.getGoalDetails().getGoalSaturday().getWater() != null)
                        goalDataBean.setSaturday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSaturday().getWater()));
                    else
                        goalDataBean.setSaturday(0f);
                    if (goalBarBean.getGoalDetails().getGoalSunday() != null && goalBarBean.getGoalDetails().getGoalSunday().getWater() != null)
                        goalDataBean.setSunday(Float.parseFloat(goalBarBean.getGoalDetails().getGoalSunday().getWater()));
                    else
                        goalDataBean.setSunday(0f);

                    addCaloriesBarDataSet(goalDataBean);
                }
            }
        }
    }

    private void setCaloriesBarChart(float goalValue) {

        binding.barChart.invalidate();
        binding.barChart.clear();
        binding.barChart.getDescription().setEnabled(false);

        binding.barChart.setPinchZoom(false);
        binding.barChart.setScaleEnabled(false);

        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setDrawBarShadow(false);

        binding.barChart.setDrawValueAboveBar(false);
        binding.barChart.setHighlightFullBarEnabled(false);

        binding.barChart.getAxisRight().setEnabled(false);
//        binding.barChart.getAxisLeft().setEnabled(false);
//        binding.barChart.getAxisLeft().setDrawLabels(false);
        binding.barChart.getAxisLeft().setDrawAxisLine(false);
        binding.barChart.getAxisLeft().setDrawGridLines(false);
//        binding.barChart.getAxisLeft().setAxisMaximum(250f);
//        binding.barChart.getAxisLeft().setAxisMinimum(0f);
        binding.barChart.getXAxis().setDrawLabels(true);
        binding.barChart.getXAxis().setDrawAxisLine(false);
        binding.barChart.getXAxis().setDrawGridLines(false);
        binding.barChart.animateXY(2000, 2000);
        LimitLine ll = new LimitLine(goalValue, "");
        ll.setLineWidth(1f);
        ll.setLineColor(getResources().getColor(R.color.graph_red));
        binding.barChart.getAxisLeft().removeAllLimitLines();
        binding.barChart.getAxisLeft().addLimitLine(ll);

        XAxis xLabels = binding.barChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getLegend().setEnabled(false);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Su");
        xAxisLabel.add("Mo");
        xAxisLabel.add("Tu");
        xAxisLabel.add("We");
        xAxisLabel.add("Th");
        xAxisLabel.add("Fr");
        xAxisLabel.add("Sa");

        xLabels.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int) value);
            }
        });

    }

    private void addCaloriesBarDataSet(GoalDataBean goalDataBean) {

        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(0f, goalDataBean.getSunday()));
        values.add(new BarEntry(1f, goalDataBean.getMonday()));
        values.add(new BarEntry(2f, goalDataBean.getTuesday()));
        values.add(new BarEntry(3f, goalDataBean.getWednesday()));
        values.add(new BarEntry(4f, goalDataBean.getThursday()));
        values.add(new BarEntry(5f, goalDataBean.getFriday()));
        values.add(new BarEntry(6f, goalDataBean.getSaturday()));

        BarDataSet set1;

        if (binding.barChart.getData() != null &&
                binding.barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setDrawValues(false);
            set1.setColor(getResources().getColor(R.color.main_color));
            binding.barChart.getData().notifyDataChanged();
            binding.barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(getResources().getColor(R.color.main_color));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            data.setValueTextColor(Color.WHITE);
            data.setBarWidth(0.5f);

            binding.barChart.setData(data);
        }

        binding.barChart.setFitBars(true);
        binding.barChart.invalidate();

    }

    private void datePicker() {
        if (dpd == null || !dpd.isVisible()) {
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int mm = now.get(Calendar.MONTH);
            int dd = now.get(Calendar.DAY_OF_MONTH);

            dpd = DatePickerDialog.newInstance(GoalsActivity.this, year, mm, dd);
            dpd.setThemeDark(false);
            dpd.vibrate(false);
            dpd.dismissOnPause(true);
            dpd.showYearPickerFirst(false);
            dpd.setVersion(DatePickerDialog.Version.VERSION_1);
            dpd.setAccentColor(ContextCompat.getColor(context, R.color.colorAccent));
            dpd.setTitle("Select date");
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ((monthOfYear+1) > 9 ? (monthOfYear+1) : ("0"+(monthOfYear+1))) + "/" + dayOfMonth + "/" + year;

        preferences.saveGoalDateData(date);
        binding.txtGoalDate.setText(date);
        callGetGoalApi(date);
    }
}
