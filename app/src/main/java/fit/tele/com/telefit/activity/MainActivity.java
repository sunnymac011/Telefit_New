package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityMainBinding;
import fit.tele.com.telefit.modelBean.CaloriesBarBean;
import fit.tele.com.telefit.modelBean.FoodCategoryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.NutritionBarBean;
import fit.tele.com.telefit.themes.MainActivityTheme;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnChartValueSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ActivityMainBinding binding;
    private float[] yData = {25.0f, 55.0f};
    private float[] yNData = {25.0f, 55.0f,68f};
    private String[] xData = {"Calories", "Burned Calories","Empty"};
    private String[] xNData = {"fat", "carb","Protein"};
    PieChart pieChart, pieChartNutrition;
    BarChart barChart, barChartNutrition;
    TextView txt_nutrients_tab,txt_calories_tab,txt_log_calories,txt_log_nutrients,txt_show_log_nutrients,txt_show_log_calories,
            txt_show_chart_calories,txt_show_chart_nutrients, txt_calories_date, txt_under, txt_daily_count, txt_food_count, txt_exercise_count,
            txt_net_count, txt_total_calories_detail, txt_nutrition_date,txt_total_calories, txt_per, txt_per_text, txt_fat_percentage, txt_carbohydrates_percentage,
            txt_protein_percentage, txt_second_text;
    LinearLayout ll_calories_details,ll_nutrients_details;
    RelativeLayout rl_calories_bar,rl_nutrients_bar;
    private CaloriesBarBean caloriesBarBean;
    private NutritionBarBean nutritionBean;
    private String dayOfTheWeek="";
    private DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat sdf = new SimpleDateFormat("EEEE");
    private DatePickerDialog dpd;
    private Calendar calendar;
    private float budgetCal = 0,burnCal = 0,netCal = 0,underCal = 0, bmr = 0, weight=0, height=0, age =0, selectedFat = 0, selectedCarb = 0,
            selectedProtin = 0, tdee = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        binding = (ActivityMainBinding) getBindingObj();
        MainActivityTheme mainActivityTheme = new MainActivityTheme();
        mainActivityTheme.setTheme(MainActivity.this);

        setListner();
    }

    private void setListner() {
        burnCal = Float.parseFloat(preferences.getBurnedCaloriesPref());
        calendar = Calendar.getInstance();
        dayOfTheWeek = sdf.format(calendar.getTime());

        pieChart = (PieChart) findViewById(R.id.caloriesPieChart);
        pieChartNutrition = (PieChart) findViewById(R.id.nutritionPieChart);
        barChart = (BarChart) findViewById(R.id.caloriesBarChart);
        barChartNutrition = (BarChart) findViewById(R.id.nutritionBarChart);
        ll_calories_details = (LinearLayout) findViewById(R.id.ll_calories_details);
        rl_calories_bar = (RelativeLayout) findViewById(R.id.rl_calories_bar);
        ll_nutrients_details = (LinearLayout) findViewById(R.id.ll_nutrients_details);
        rl_nutrients_bar = (RelativeLayout) findViewById(R.id.rl_nutrients_bar);
        txt_nutrients_tab = (TextView) findViewById(R.id.txt_nutrients_tab);
        txt_nutrients_tab.setOnClickListener(this);
        txt_under = (TextView) findViewById(R.id.txt_under);
        txt_second_text = (TextView) findViewById(R.id.txt_second_text);
        txt_daily_count = (TextView) findViewById(R.id.txt_daily_count);
        txt_food_count = (TextView) findViewById(R.id.txt_food_count);
        txt_exercise_count = (TextView) findViewById(R.id.txt_exercise_count);
        txt_net_count = (TextView) findViewById(R.id.txt_net_count);
        txt_total_calories_detail = (TextView) findViewById(R.id.txt_total_calories_detail);
        txt_total_calories = (TextView) findViewById(R.id.txt_total_calories);
        txt_per = (TextView) findViewById(R.id.txt_per);
        txt_per_text = (TextView) findViewById(R.id.txt_per_text);
        txt_fat_percentage = (TextView) findViewById(R.id.txt_fat_percentage);
        txt_carbohydrates_percentage = (TextView) findViewById(R.id.txt_carbohydrates_percentage);
        txt_carbohydrates_percentage = (TextView) findViewById(R.id.txt_carbohydrates_percentage);
        txt_protein_percentage = (TextView) findViewById(R.id.txt_protein_percentage);
        txt_calories_tab = (TextView) findViewById(R.id.txt_calories_tab);
        txt_calories_tab.setOnClickListener(this);
        txt_log_calories = (TextView) findViewById(R.id.txt_log_calories);
        txt_log_calories.setOnClickListener(this);
        txt_log_nutrients = (TextView) findViewById(R.id.txt_log_nutrients);
        txt_log_nutrients.setOnClickListener(this);
        txt_show_log_calories = (TextView) findViewById(R.id.txt_show_log_calories);
        txt_show_log_calories.setOnClickListener(this);
        txt_show_log_nutrients = (TextView) findViewById(R.id.txt_show_log_nutrients);
        txt_show_log_nutrients.setOnClickListener(this);
        txt_show_chart_calories = (TextView) findViewById(R.id.txt_show_chart_calories);
        txt_show_chart_calories.setOnClickListener(this);
        txt_show_chart_nutrients = (TextView) findViewById(R.id.txt_show_chart_nutrients);
        txt_show_chart_nutrients.setOnClickListener(this);
        txt_calories_date = (TextView) findViewById(R.id.txt_calories_date);
        txt_calories_date.setOnClickListener(this);
        txt_calories_date.setText(format1.format(calendar.getTime()));
        txt_nutrition_date = (TextView) findViewById(R.id.txt_nutrition_date);
        txt_nutrition_date.setOnClickListener(this);
        txt_nutrition_date.setText(format1.format(calendar.getTime()));

        binding.llProfile.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        callGetCaloriesApi(format1.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.txt_calories_tab:
                binding.vf.setDisplayedChild(0);
                callGetCaloriesApi(format1.format(calendar.getTime()));
                break;

            case R.id.txt_nutrients_tab:
                binding.vf.setDisplayedChild(1);
                callGetNutritionApi(format1.format(calendar.getTime()));
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

            case R.id.ll_goals:
                intent = new Intent(context, GoalsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.txt_log_calories:
                intent = new Intent(context, FoodCalNutActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.txt_log_nutrients:
                intent = new Intent(context, FoodCalNutActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.txt_show_log_calories:



                ll_calories_details.setVisibility(View.VISIBLE);
                rl_calories_bar.setVisibility(View.GONE);
                break;

            case R.id.txt_show_log_nutrients:
                ll_nutrients_details.setVisibility(View.VISIBLE);
                rl_nutrients_bar.setVisibility(View.GONE);
                float totalNutrition = selectedFat+selectedCarb+selectedProtin;
                float perFat = (100*selectedFat)/totalNutrition;
                float perCarb = (100*selectedCarb)/totalNutrition;
                float perProtin = (100*selectedProtin)/totalNutrition;
                if (String.format("%.2f", perFat).equalsIgnoreCase("NaN"))
                    txt_fat_percentage.setText("0%");
                else
                    txt_fat_percentage.setText(String.format("%.2f", perFat)+"%");
                if (String.format("%.2f", perCarb).equalsIgnoreCase("NaN"))
                    txt_carbohydrates_percentage.setText("0%");
                else
                    txt_carbohydrates_percentage.setText(String.format("%.2f", perCarb)+"%");
                if (String.format("%.2f", perProtin).equalsIgnoreCase("NaN"))
                    txt_protein_percentage.setText("0%");
                else
                    txt_protein_percentage.setText(String.format("%.2f", perProtin)+"%");
                break;

            case R.id.txt_show_chart_calories:
                ll_calories_details.setVisibility(View.GONE);
                rl_calories_bar.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_show_chart_nutrients:
                ll_nutrients_details.setVisibility(View.GONE);
                rl_nutrients_bar.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_calories_date:
                datePicker();
                break;

            case R.id.txt_nutrition_date:
                datePicker();
                break;
        }
    }

    private void setCaloriesBarChart() {

        if (preferences.getUserDataPref().getWeightType().equalsIgnoreCase("kg"))
            weight = Float.parseFloat(preferences.getUserDataPref().getWeight());
        else
            weight = (float) (Float.parseFloat(preferences.getUserDataPref().getWeight())*0.453592);
        if (preferences.getUserDataPref().getHeight() != null)
        {
            if (preferences.getUserDataPref().getHeightType() != null) {
                if (preferences.getUserDataPref().getHeightType().equalsIgnoreCase("ft")) {
                    String strFT="0",strIN="0";
                    String[] separated = preferences.getUserDataPref().getHeight().split("'");
                    if (separated.length > 0)
                        strFT = separated[0];
                    if (separated.length > 1)
                        strIN = separated[1]!=null?separated[1]:"0";
                    int intFT = Integer.parseInt(strFT);
                    int intInch = Integer.parseInt(strIN);
                    height = (float) (((12*intFT)+intInch)*2.54);
                }
                else
                    height = Float.parseFloat(preferences.getUserDataPref().getHeight());
            }

        }
        if (preferences.getUserDataPref().getDob() != null)
        {
            String[] items1 = preferences.getUserDataPref().getDob().split("[/-]");
            String year = items1[0];
            String month = items1[1];
            String date1 = items1[2];
            age = CommonUtils.getAge(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(date1));
        }

        if (preferences.getUserDataPref().getGender().equalsIgnoreCase("male"))
            bmr = (float)(66+(13.7*weight)+(5*height)-(6.8*age));
        else
            bmr = (float)(655+(9.6*weight)+(1.6*height)-(4.7*age));

        if (preferences.getUserDataPref() != null && preferences.getUserDataPref().getActivity() != null
                && !TextUtils.isEmpty(preferences.getUserDataPref().getActivity())) {
            if (preferences.getUserDataPref().getActivity().equalsIgnoreCase("Sedentary"))
                tdee = (float)(bmr*1.2);
            if (preferences.getUserDataPref().getActivity().equalsIgnoreCase("Lightly active"))
                tdee = (float)(bmr*1.375);
            if (preferences.getUserDataPref().getActivity().equalsIgnoreCase("Moderately active"))
                tdee = (float)(bmr*1.55);
            if (preferences.getUserDataPref().getActivity().equalsIgnoreCase("Very active"))
                tdee = (float)(bmr*1.725);
            if (preferences.getUserDataPref().getActivity().equalsIgnoreCase("Extra active"))
                tdee = (float)(bmr*1.9);
        }

        if (preferences.getUserDataPref() != null && preferences.getUserDataPref().getMaintainWeight() != null
                && !TextUtils.isEmpty(preferences.getUserDataPref().getMaintainWeight())) {
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Lose 1 pound per week"))
                budgetCal = (float)(tdee*0.20);
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Lose 1.5 pounds per week"))
                budgetCal = (float)(tdee*0.30);
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Lose 2 pounds per week"))
                budgetCal = (float)(tdee*0.35);
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Gain 0.5 pound per week"))
                budgetCal = (float)(tdee+250);
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Gain 1 pound per week"))
                budgetCal = (float)(tdee+500);
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Gain 1.5 pounds per week"))
                budgetCal = (float)(tdee+750);
        }

//        RoundedBarChartRenderer roundedBarChartRenderer= new RoundedBarChartRenderer(barChart,barChart.getAnimator(),barChart.getViewPortHandler());
//        roundedBarChartRenderer.setmRadius(20f);
//        barChart.setRenderer(roundedBarChartRenderer);

        barChart.setOnChartValueSelectedListener(this);

        barChart.getDescription().setEnabled(false);

        barChart.setMaxVisibleValueCount(40);

        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

        barChart.setDrawValueAboveBar(false);
        barChart.setHighlightFullBarEnabled(false);

        barChart.getAxisRight().setEnabled(false);
//        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.animateXY(2000, 2000);
        LimitLine ll = new LimitLine(budgetCal, "");
        ll.setLineWidth(0.5f);
        ll.setLineColor(context.getResources().getColor(R.color.light_gray));
        barChart.getAxisLeft().addLimitLine(ll);

        XAxis xLabels = barChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getLegend().setEnabled(false);

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

    private void addCaloriesBarDataSet() {

        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(
                0,
                new float[]{caloriesBarBean.getSunday()}));

        values.add(new BarEntry(
                1,
                new float[]{caloriesBarBean.getMonday()}));

        values.add(new BarEntry(
                2,
                new float[]{caloriesBarBean.getTuesday()}));

        values.add(new BarEntry(
                3,
                new float[]{caloriesBarBean.getWednesday()}));

        values.add(new BarEntry(
                4,
                new float[]{caloriesBarBean.getThursday()}));

        values.add(new BarEntry(
                5,
                new float[]{caloriesBarBean.getFriday()}));

        values.add(new BarEntry(
                6,
                new float[]{caloriesBarBean.getSaturday()}));

        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setDrawValues(false);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);

            ArrayList<Integer> colors = new ArrayList<>();
            if (dayOfTheWeek.equalsIgnoreCase("sunday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getSunday();
                if (caloriesBarBean.getSunday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getSunday()));
                netCal = caloriesBarBean.getSunday() - burnCal;

                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getSunday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getSunday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));
            if (dayOfTheWeek.equalsIgnoreCase("monday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getMonday();
                if (caloriesBarBean.getMonday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getMonday()));
                netCal = caloriesBarBean.getMonday() - burnCal;
                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getMonday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getMonday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));
            if (dayOfTheWeek.equalsIgnoreCase("tuesday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getTuesday();
                if (caloriesBarBean.getTuesday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getTuesday()));
                netCal = caloriesBarBean.getTuesday() - burnCal;
                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getTuesday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getTuesday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));
            if (dayOfTheWeek.equalsIgnoreCase("wednesday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getWednesday();
                if (caloriesBarBean.getWednesday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getWednesday()));
                netCal = caloriesBarBean.getWednesday() - burnCal;
                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getWednesday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getWednesday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));
            if (dayOfTheWeek.equalsIgnoreCase("thursday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getThursday();
                if (caloriesBarBean.getThursday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getThursday()));
                netCal = caloriesBarBean.getThursday() - burnCal;
                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getThursday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getThursday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));
            if (dayOfTheWeek.equalsIgnoreCase("friday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getFriday();
                if (caloriesBarBean.getFriday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getFriday()));
                netCal = caloriesBarBean.getFriday() - burnCal;
                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getFriday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getFriday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));
            if (dayOfTheWeek.equalsIgnoreCase("saturday"))
            {
                colors.add(getResources().getColor(R.color.light_blue_text));
                yData[0] = caloriesBarBean.getSaturday();
                if (caloriesBarBean.getSaturday() > budgetCal)
                    yData[1] = 0;
                else
                    yData[1] = budgetCal;
                txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getSaturday()));
                netCal = caloriesBarBean.getSaturday() - burnCal;
                txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getSaturday())+" of total "+(int)budgetCal+" calories utilized.");
                txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getSaturday())+" of total "+(int)budgetCal+" calories utilized.");
            }
            else
                colors.add(getResources().getColor(R.color.colorAccent));

            underCal = budgetCal - netCal;
            if (underCal > budgetCal)
            {
                txt_under.setTextColor(getResources().getColor(R.color.red));
                txt_second_text.setText("calories over budget");
            }
            else
            {
                txt_under.setTextColor(getResources().getColor(R.color.light_blue_text));
                txt_second_text.setText("calories under budget");
            }

            txt_under.setText(""+(int) Math.abs(underCal));
            txt_daily_count.setText(""+String.format("%.2f", budgetCal));
            txt_exercise_count.setText(""+String.format("%.2f", burnCal));
            txt_net_count.setText(""+String.format("%.2f", netCal));
            set1.setColors(colors);
//            set1.setStackLabels(new String[]{"Calories", "Burned Calories"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            data.setValueTextColor(Color.WHITE);
            data.setBarWidth(0.6f);

            barChart.setData(data);
        }

        barChart.setFitBars(true);
        barChart.invalidate();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                final String x = barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), barChart.getXAxis());
                if (x.equalsIgnoreCase("su"))
                {
                    yData[0] = caloriesBarBean.getSunday();
                    if (caloriesBarBean.getSunday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getSunday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getSunday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getSunday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getSunday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }
                if (x.equalsIgnoreCase("mo"))
                {
                    yData[0] = caloriesBarBean.getMonday();
                    if (caloriesBarBean.getMonday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getMonday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getMonday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getMonday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getMonday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }
                if (x.equalsIgnoreCase("tu"))
                {
                    yData[0] = caloriesBarBean.getTuesday();
                    if (caloriesBarBean.getTuesday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getTuesday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getTuesday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getTuesday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getTuesday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }
                if (x.equalsIgnoreCase("we"))
                {
                    yData[0] = caloriesBarBean.getWednesday();
                    if (caloriesBarBean.getWednesday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getWednesday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getWednesday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getWednesday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getWednesday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }
                if (x.equalsIgnoreCase("th"))
                {
                    yData[0] = caloriesBarBean.getThursday();
                    if (caloriesBarBean.getThursday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getThursday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getThursday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getThursday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getThursday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }
                if (x.equalsIgnoreCase("fr"))
                {
                    yData[0] = caloriesBarBean.getFriday();
                    if (caloriesBarBean.getFriday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getFriday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getFriday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getFriday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getFriday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }
                if (x.equalsIgnoreCase("sa"))
                {
                    yData[0] = caloriesBarBean.getSaturday();
                    if (caloriesBarBean.getSaturday() > budgetCal)
                        yData[1] = 0;
                    else
                        yData[1] = budgetCal;
                    netCal = caloriesBarBean.getSaturday() - burnCal;
                    txt_food_count.setText(""+String.format("%.2f", caloriesBarBean.getSaturday()));
                    txt_total_calories_detail.setText(String.format("%.2f", caloriesBarBean.getSaturday())+" of total "+(int)budgetCal+" calories utilized.");
                    txt_total_calories.setText(String.format("%.2f", caloriesBarBean.getSaturday())+" of total "+(int)budgetCal+" calories utilized.");
                    addCaloriesPieDataSet();
                }

                underCal = budgetCal - netCal;
                if (underCal > budgetCal)
                {
                    txt_under.setTextColor(getResources().getColor(R.color.red));
                    txt_second_text.setText("calories over budget");
                }
                else
                {
                    txt_under.setTextColor(getResources().getColor(R.color.light_blue_text));
                    txt_second_text.setText("calories under budget");
                }
                txt_under.setText(""+(int) Math.abs(underCal));
                txt_daily_count.setText(""+String.format("%.2f", budgetCal));
                txt_exercise_count.setText(""+String.format("%.2f", burnCal));
                txt_net_count.setText(""+String.format("%.2f", netCal));
            }



            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setCaloriesPieChart() {

        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(65f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterTextSize(10);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.animateX(1600);
        pieChart.animateY(1600);

    }

    private void addCaloriesPieDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(0);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        if (yData[0] > budgetCal)
            colors.add(getResources().getColor(R.color.graph_red));
        else
            colors.add(getResources().getColor(R.color.light_blue_text));
        colors.add(getResources().getColor(R.color.empty_chart));

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void setNutritionPieChart() {
        pieChartNutrition.getDescription().setEnabled(false);
        pieChartNutrition.setRotationEnabled(false);
        pieChartNutrition.setHoleRadius(65f);
        pieChartNutrition.setTransparentCircleAlpha(0);
        pieChartNutrition.setCenterTextSize(10);
        pieChartNutrition.getLegend().setEnabled(false);
        pieChartNutrition.setDrawSliceText(false);
        pieChartNutrition.animateX(1600);
        pieChartNutrition.animateY(1600);

    }

    private void addNutritionPieDataSet() {
        float totalNutrition = selectedFat+selectedCarb+selectedProtin;
        float perNutrition = (100*selectedFat)/totalNutrition;
        if (String.format("%.2f", perNutrition).equalsIgnoreCase("NaN"))
            txt_per.setText("0%");
        else
            txt_per.setText(""+String.format("%.2f", perNutrition)+"%");
        txt_per.setTextColor(getResources().getColor(R.color.yellow));
        txt_per_text.setText("Fat");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yNData.length; i++){
            yEntrys.add(new PieEntry(yNData[i] , i));
        }

        for(int i = 1; i < xNData.length; i++){
            xEntrys.add(xNData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(0);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.yellow));
        colors.add(getResources().getColor(R.color.light_blue_text));
        colors.add(getResources().getColor(R.color.purple));

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieChartNutrition.setData(pieData);
        pieChartNutrition.invalidate();

        pieChartNutrition.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float totalNutrition = selectedFat+selectedCarb+selectedProtin;
                if (h.getX() == 0.0) {
                    float perNutrition = (100*selectedFat)/totalNutrition;
                    if (String.format("%.2f", perNutrition).equalsIgnoreCase("NaN"))
                        txt_per.setText("0%");
                    else
                        txt_per.setText(""+String.format("%.2f", perNutrition)+"%");
                    txt_per.setTextColor(getResources().getColor(R.color.yellow));
                    txt_per_text.setText("Fat");
                }
                if (h.getX() == 1.0) {
                    float perNutrition = (100*selectedCarb)/totalNutrition;
                    if (String.format("%.2f", perNutrition).equalsIgnoreCase("NaN"))
                        txt_per.setText("0%");
                    else
                        txt_per.setText(""+String.format("%.2f", perNutrition)+"%");
                    txt_per.setTextColor(getResources().getColor(R.color.light_blue_text));
                    txt_per_text.setText("Carbs");
                }
                if (h.getX() == 2.0) {
                    float perNutrition = (100*selectedProtin)/totalNutrition;
                    if (String.format("%.2f", perNutrition).equalsIgnoreCase("NaN"))
                        txt_per.setText("0%");
                    else
                        txt_per.setText(""+String.format("%.2f", perNutrition)+"%");
                    txt_per.setTextColor(getResources().getColor(R.color.purple));
                    txt_per_text.setText("Protein");
                }
            }

            @Override
            public void onNothingSelected() {
                float totalNutrition = selectedFat+selectedCarb+selectedProtin;
                float perNutrition = (100*selectedFat)/totalNutrition;
                if (String.format("%.2f", perNutrition).equalsIgnoreCase("NaN"))
                    txt_per.setText("0%");
                else
                    txt_per.setText(""+String.format("%.2f", perNutrition)+"%");
                txt_per.setTextColor(getResources().getColor(R.color.yellow));
                txt_per_text.setText("Fat");
            }
        });
    }

    private void setNutritionBarChart() {

//        RoundedBarChartRenderer roundedBarChartRenderer= new RoundedBarChartRenderer(barChart,barChart.getAnimator(),barChart.getViewPortHandler());
//        roundedBarChartRenderer.setmRadius(20f);
//        barChart.setRenderer(roundedBarChartRenderer);

        barChartNutrition.setOnChartValueSelectedListener(this);

        barChartNutrition.getDescription().setEnabled(false);

        barChartNutrition.setMaxVisibleValueCount(40);

        barChartNutrition.setPinchZoom(false);
        barChartNutrition.setScaleEnabled(false);

        barChartNutrition.setDrawGridBackground(false);
        barChartNutrition.setDrawBarShadow(false);

        barChartNutrition.setDrawValueAboveBar(false);
        barChartNutrition.setHighlightFullBarEnabled(false);

        barChartNutrition.getAxisRight().setEnabled(false);
//        barChart.getAxisLeft().setEnabled(false);
        barChartNutrition.getAxisLeft().setDrawLabels(false);
        barChartNutrition.getAxisLeft().setDrawAxisLine(false);
        barChartNutrition.getAxisLeft().setDrawGridLines(false);
        barChartNutrition.getXAxis().setDrawLabels(true);
        barChartNutrition.getXAxis().setDrawAxisLine(false);
        barChartNutrition.getXAxis().setDrawGridLines(false);
        barChartNutrition.animateXY(2000, 2000);

        XAxis xLabels = barChartNutrition.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartNutrition.getLegend().setEnabled(false);

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

    private void addNutritionBarDataSet() {

        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(
                0,
                new float[]{nutritionBean.getSunday().getFat(),nutritionBean.getSunday().getCarbohydrates(), nutritionBean.getSunday().getProteins()}));

        values.add(new BarEntry(
                1,
                new float[]{nutritionBean.getMonday().getFat(),nutritionBean.getMonday().getCarbohydrates(), nutritionBean.getMonday().getProteins()}));

        values.add(new BarEntry(
                2,
                new float[]{nutritionBean.getTuesday().getFat(),nutritionBean.getTuesday().getCarbohydrates(), nutritionBean.getTuesday().getProteins()}));

        values.add(new BarEntry(
                3,
                new float[]{nutritionBean.getWednesday().getFat(),nutritionBean.getWednesday().getCarbohydrates(), nutritionBean.getWednesday().getProteins()}));

        values.add(new BarEntry(
                4,
                new float[]{nutritionBean.getThursday().getFat(),nutritionBean.getThursday().getCarbohydrates(), nutritionBean.getThursday().getProteins()}));

        values.add(new BarEntry(
                5,
                new float[]{nutritionBean.getFriday().getFat(),nutritionBean.getFriday().getCarbohydrates(), nutritionBean.getFriday().getProteins()}));

        values.add(new BarEntry(
                6,
                new float[]{nutritionBean.getSaturday().getFat(),nutritionBean.getSaturday().getCarbohydrates(), nutritionBean.getSaturday().getProteins()}));

        BarDataSet set1;

        if (dayOfTheWeek.equalsIgnoreCase("sunday"))
        {
            yNData[0] = nutritionBean.getSunday().getFat();
            yNData[1] = nutritionBean.getSunday().getCarbohydrates();
            yNData[2] = nutritionBean.getSunday().getProteins();
            selectedFat = nutritionBean.getSunday().getFat();
            selectedCarb = nutritionBean.getSunday().getCarbohydrates();
            selectedProtin = nutritionBean.getSunday().getProteins();
        }
        if (dayOfTheWeek.equalsIgnoreCase("monday"))
        {
            yNData[0] = nutritionBean.getMonday().getFat();
            yNData[1] = nutritionBean.getMonday().getCarbohydrates();
            yNData[2] = nutritionBean.getMonday().getProteins();
            selectedFat = nutritionBean.getMonday().getFat();
            selectedCarb = nutritionBean.getMonday().getCarbohydrates();
            selectedProtin = nutritionBean.getMonday().getProteins();
        }
        if (dayOfTheWeek.equalsIgnoreCase("tuesday"))
        {
            yNData[0] = nutritionBean.getTuesday().getFat();
            yNData[1] = nutritionBean.getTuesday().getCarbohydrates();
            yNData[2] = nutritionBean.getTuesday().getProteins();
            selectedFat = nutritionBean.getTuesday().getFat();
            selectedCarb = nutritionBean.getTuesday().getCarbohydrates();
            selectedProtin = nutritionBean.getTuesday().getProteins();
        }
        if (dayOfTheWeek.equalsIgnoreCase("wednesday"))
        {
            yNData[0] = nutritionBean.getWednesday().getFat();
            yNData[1] = nutritionBean.getWednesday().getCarbohydrates();
            yNData[2] = nutritionBean.getWednesday().getProteins();
            selectedFat = nutritionBean.getWednesday().getFat();
            selectedCarb = nutritionBean.getWednesday().getCarbohydrates();
            selectedProtin = nutritionBean.getWednesday().getProteins();
        }
        if (dayOfTheWeek.equalsIgnoreCase("thursday"))
        {
            yNData[0] = nutritionBean.getThursday().getFat();
            yNData[1] = nutritionBean.getThursday().getCarbohydrates();
            yNData[2] = nutritionBean.getThursday().getProteins();
            selectedFat = nutritionBean.getThursday().getFat();
            selectedCarb = nutritionBean.getThursday().getCarbohydrates();
            selectedProtin = nutritionBean.getThursday().getProteins();
        }
        if (dayOfTheWeek.equalsIgnoreCase("friday"))
        {
            yNData[0] = nutritionBean.getFriday().getFat();
            yNData[1] = nutritionBean.getFriday().getCarbohydrates();
            yNData[2] = nutritionBean.getFriday().getProteins();
            selectedFat = nutritionBean.getFriday().getFat();
            selectedCarb = nutritionBean.getFriday().getCarbohydrates();
            selectedProtin = nutritionBean.getFriday().getProteins();
        }
        if (dayOfTheWeek.equalsIgnoreCase("saturday"))
        {
            yNData[0] = nutritionBean.getSaturday().getFat();
            yNData[1] = nutritionBean.getSaturday().getCarbohydrates();
            yNData[2] = nutritionBean.getSaturday().getProteins();
            selectedFat = nutritionBean.getSaturday().getFat();
            selectedCarb = nutritionBean.getSaturday().getCarbohydrates();
            selectedProtin = nutritionBean.getSaturday().getProteins();
        }

        if (barChartNutrition.getData() != null &&
                barChartNutrition.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChartNutrition.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setDrawValues(false);
            barChartNutrition.getData().notifyDataChanged();
            barChartNutrition.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);

            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Fat", "Carb", "Protein"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            data.setValueTextColor(Color.WHITE);
            data.setBarWidth(0.6f);

            barChartNutrition.setData(data);
        }

        barChartNutrition.setFitBars(true);
        barChartNutrition.invalidate();

        barChartNutrition.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                final String x = barChartNutrition.getXAxis().getValueFormatter().getFormattedValue(e.getX(), barChartNutrition.getXAxis());
                if (x.equalsIgnoreCase("su"))
                {
                    yNData[0] = nutritionBean.getSunday().getFat();
                    yNData[1] = nutritionBean.getSunday().getCarbohydrates();
                    yNData[2] = nutritionBean.getSunday().getProteins();
                    selectedFat = nutritionBean.getSunday().getFat();
                    selectedCarb = nutritionBean.getSunday().getCarbohydrates();
                    selectedProtin = nutritionBean.getSunday().getProteins();
                    addNutritionPieDataSet();
                }
                if (x.equalsIgnoreCase("mo"))
                {
                    yNData[0] = nutritionBean.getMonday().getFat();
                    yNData[1] = nutritionBean.getMonday().getCarbohydrates();
                    yNData[2] = nutritionBean.getMonday().getProteins();
                    selectedFat = nutritionBean.getMonday().getFat();
                    selectedCarb = nutritionBean.getMonday().getCarbohydrates();
                    selectedProtin = nutritionBean.getMonday().getProteins();
                    addNutritionPieDataSet();
                }
                if (x.equalsIgnoreCase("tu"))
                {
                    yNData[0] = nutritionBean.getTuesday().getFat();
                    yNData[1] = nutritionBean.getTuesday().getCarbohydrates();
                    yNData[2] = nutritionBean.getTuesday().getProteins();
                    selectedFat = nutritionBean.getTuesday().getFat();
                    selectedCarb = nutritionBean.getTuesday().getCarbohydrates();
                    selectedProtin = nutritionBean.getTuesday().getProteins();
                    addNutritionPieDataSet();
                }
                if (x.equalsIgnoreCase("we"))
                {
                    yNData[0] = nutritionBean.getWednesday().getFat();
                    yNData[1] = nutritionBean.getWednesday().getCarbohydrates();
                    yNData[2] = nutritionBean.getWednesday().getProteins();
                    selectedFat = nutritionBean.getWednesday().getFat();
                    selectedCarb = nutritionBean.getWednesday().getCarbohydrates();
                    selectedProtin = nutritionBean.getWednesday().getProteins();
                    addNutritionPieDataSet();
                }
                if (x.equalsIgnoreCase("th"))
                {
                    yNData[0] = nutritionBean.getThursday().getFat();
                    yNData[1] = nutritionBean.getThursday().getCarbohydrates();
                    yNData[2] = nutritionBean.getThursday().getProteins();
                    selectedFat = nutritionBean.getThursday().getFat();
                    selectedCarb = nutritionBean.getThursday().getCarbohydrates();
                    selectedProtin = nutritionBean.getThursday().getProteins();
                    addNutritionPieDataSet();
                }
                if (x.equalsIgnoreCase("fr"))
                {
                    yNData[0] = nutritionBean.getFriday().getFat();
                    yNData[1] = nutritionBean.getFriday().getCarbohydrates();
                    yNData[2] = nutritionBean.getFriday().getProteins();
                    selectedFat = nutritionBean.getFriday().getFat();
                    selectedCarb = nutritionBean.getFriday().getCarbohydrates();
                    selectedProtin = nutritionBean.getFriday().getProteins();
                    addNutritionPieDataSet();
                }
                if (x.equalsIgnoreCase("sa"))
                {
                    yNData[0] = nutritionBean.getSaturday().getFat();
                    yNData[1] = nutritionBean.getSaturday().getCarbohydrates();
                    yNData[2] = nutritionBean.getSaturday().getProteins();
                    selectedFat = nutritionBean.getSaturday().getFat();
                    selectedCarb = nutritionBean.getSaturday().getCarbohydrates();
                    selectedProtin = nutritionBean.getSaturday().getProteins();
                    addNutritionPieDataSet();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

//        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        colors[0] = getResources().getColor(R.color.yellow);
        colors[1] = getResources().getColor(R.color.light_blue_text);
        colors[2] = getResources().getColor(R.color.purple);

        return colors;
    }

    private void callGetCaloriesApi(String strDate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", "2");
            map.put("meal_date", strDate);

            Observable<ModelBean<CaloriesBarBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCaloriesApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<CaloriesBarBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetCaloriesApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<CaloriesBarBean> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                caloriesBarBean = apiFoodBean.getResult();
                                setCaloriesBarChart();
                                addCaloriesBarDataSet();

                                setCaloriesPieChart();
                                addCaloriesPieDataSet();
                            }
                            else
                                CommonUtils.toast(context, ""+apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callGetNutritionApi(String strDate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", "2");
            map.put("meal_date", strDate);

            Observable<ModelBean<NutritionBarBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getNutritionApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<NutritionBarBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetNutritionApi",e.getMessage()+"    "+e.getCause());
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<NutritionBarBean> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                nutritionBean = apiFoodBean.getResult();
                                setNutritionBarChart();
                                addNutritionBarDataSet();

                                setNutritionPieChart();
                                addNutritionPieDataSet();
                            }
                            else
                                CommonUtils.toast(context, ""+apiFoodBean.getMessage());
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

            dpd = DatePickerDialog.newInstance(MainActivity.this, year, mm, dd);
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

        if (binding.vf.getDisplayedChild() == 0) {
            txt_calories_date.setText(date);
            callGetCaloriesApi(date);
        }else {
            txt_nutrition_date.setText(date);
            callGetNutritionApi(date);
        }
//        strSelectedDate = date;
//        try {
//            Date newDate = format1.parse(strSelectedDate);
//            strSelectedDate = apiformat.format(newDate);
//            callMealDateApi(strSelectedDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

}
