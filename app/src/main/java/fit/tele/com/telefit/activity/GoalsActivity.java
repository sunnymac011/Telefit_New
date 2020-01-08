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
import fit.tele.com.telefit.modelBean.GoalBarBean;
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
                                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().size() > 0)
                                {
                                    
                                }
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void setLineChartData(String selectedNutrition){
        if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().size() > 0)
        {
            if (selectedNutrition.equalsIgnoreCase("Protein")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Carbs")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Fat")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Cholesterol")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Fiber")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Weight")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Body Fat")) {
                
            }
            if (selectedNutrition.equalsIgnoreCase("Water")) {
                
            }
        }
    }

//    private void setCaloriesbinding.bChart() {
//
//        binding.bChart.getDescription().setEnabled(false);
//
//        binding.bChart.setMaxVisibleValueCount(40);
//
//        binding.bChart.setPinchZoom(false);
//        binding.bChart.setScaleEnabled(false);
//
//        binding.bChart.setDrawGridBackground(false);
//        binding.bChart.setDrawBarShadow(false);
//
//        binding.bChart.setDrawValueAboveBar(false);
//        binding.bChart.setHighlightFullBarEnabled(false);
//
//        binding.bChart.getAxisRight().setEnabled(false);
////        binding.bChart.getAxisLeft().setEnabled(false);
//        binding.bChart.getAxisLeft().setDrawLabels(false);
//        binding.bChart.getAxisLeft().setDrawAxisLine(false);
//        binding.bChart.getAxisLeft().setDrawGridLines(false);
//        binding.bChart.getXAxis().setDrawLabels(true);
//        binding.bChart.getXAxis().setDrawAxisLine(false);
//        binding.bChart.getXAxis().setDrawGridLines(false);
//        binding.bChart.animateXY(2000, 2000);
//        LimitLine ll = new LimitLine(budgetCal, "");
//        ll.setLineWidth(0.5f);
//        ll.setLineColor(context.getResources().getColor(R.color.light_gray));
//        binding.bChart.getAxisLeft().addLimitLine(ll);
//
//        XAxis xLabels = binding.bChart.getXAxis();
//        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
//        binding.bChart.getLegend().setEnabled(false);
//
//        final ArrayList<String> xAxisLabel = new ArrayList<>();
//        xAxisLabel.add("Su");
//        xAxisLabel.add("Mo");
//        xAxisLabel.add("Tu");
//        xAxisLabel.add("We");
//        xAxisLabel.add("Th");
//        xAxisLabel.add("Fr");
//        xAxisLabel.add("Sa");
//
//        xLabels.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xAxisLabel.get((int) value);
//            }
//        });
//
//    }
//
//    private void addCaloriesBarDataSet() {
//
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        values.add(new BarEntry(
//                0,
//                new float[]{caloriesBarBean.getSunday()}));
//
//        values.add(new BarEntry(
//                1,
//                new float[]{caloriesBarBean.getMonday()}));
//
//        values.add(new BarEntry(
//                2,
//                new float[]{caloriesBarBean.getTuesday()}));
//
//        values.add(new BarEntry(
//                3,
//                new float[]{caloriesBarBean.getWednesday()}));
//
//        values.add(new BarEntry(
//                4,
//                new float[]{caloriesBarBean.getThursday()}));
//
//        values.add(new BarEntry(
//                5,
//                new float[]{caloriesBarBean.getFriday()}));
//
//        values.add(new BarEntry(
//                6,
//                new float[]{caloriesBarBean.getSaturday()}));
//
//        BarDataSet set1;
//
//        if (binding.bChart.getData() != null &&
//                binding.bChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) binding.bChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            set1.setDrawValues(false);
//            binding.bChart.getData().notifyDataChanged();
//            binding.bChart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(values, "");
//            set1.setDrawIcons(false);
//            set1.setDrawValues(false);
//
//            ArrayList<Integer> colors = new ArrayList<>();
//            if (dayOfTheWeek.equalsIgnoreCase("sunday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getSunday();
//                if (caloriesBarBean.getSunday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//            if (dayOfTheWeek.equalsIgnoreCase("monday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getMonday();
//                if (caloriesBarBean.getMonday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//            if (dayOfTheWeek.equalsIgnoreCase("tuesday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getTuesday();
//                if (caloriesBarBean.getTuesday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//            if (dayOfTheWeek.equalsIgnoreCase("wednesday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getWednesday();
//                if (caloriesBarBean.getWednesday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//            if (dayOfTheWeek.equalsIgnoreCase("thursday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getThursday();
//                if (caloriesBarBean.getThursday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//            if (dayOfTheWeek.equalsIgnoreCase("friday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getFriday();
//                if (caloriesBarBean.getFriday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//            if (dayOfTheWeek.equalsIgnoreCase("saturday"))
//            {
//                colors.add(getResources().getColor(R.color.light_blue_text));
//                yData[0] = caloriesBarBean.getSaturday();
//                if (caloriesBarBean.getSaturday() > budgetCal)
//                    yData[1] = 0;
//                else
//                    yData[1] = budgetCal;
//            }
//            else
//                colors.add(getResources().getColor(R.color.colorAccent));
//
//            set1.setColors(colors);
////            set1.setStackLabels(new String[]{"Calories", "Burned Calories"});
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
//            data.setValueTextColor(Color.WHITE);
//            data.setBarWidth(0.6f);
//
//            binding.bChart.setData(data);
//        }
//
//        binding.bChart.setFitBars(true);
//        binding.bChart.invalidate();
//
//    }

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
