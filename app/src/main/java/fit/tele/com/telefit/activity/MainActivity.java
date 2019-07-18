package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
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

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityMainBinding;
import fit.tele.com.telefit.themes.MainActivityTheme;

public class MainActivity extends BaseActivity implements OnChartValueSelectedListener, View.OnClickListener {

    ActivityMainBinding binding;
    private float[] yData = {25.0f, 45.0f, 30.0f};
    private String[] xData = {"Calories", "Burned Calories","Empty"};
    PieChart pieChart;
    BarChart barChart;
    TextView txt_nutrients_tab,txt_calories_tab,txt_log_calories,txt_log_nutrients,txt_show_log_nutrients,txt_show_log_calories,
            txt_show_chart_calories,txt_show_chart_nutrients;
    LinearLayout ll_calories_details,ll_nutrients_details;
    RelativeLayout rl_calories_bar,rl_nutrients_bar;

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

        setCaloriesPieChart();
        addCaloriesPieDataSet();
        setCaloriesBarChart();
        addCaloriesBarDataSet();
    }

    private void setListner() {
        pieChart = (PieChart) findViewById(R.id.caloriesPieChart);
        barChart = (BarChart) findViewById(R.id.caloriesBarChart);
        ll_calories_details = (LinearLayout) findViewById(R.id.ll_calories_details);
        rl_calories_bar = (RelativeLayout) findViewById(R.id.rl_calories_bar);
        ll_nutrients_details = (LinearLayout) findViewById(R.id.ll_nutrients_details);
        rl_nutrients_bar = (RelativeLayout) findViewById(R.id.rl_nutrients_bar);
        txt_nutrients_tab = (TextView) findViewById(R.id.txt_nutrients_tab);
        txt_nutrients_tab.setOnClickListener(this);
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

        binding.llProfile.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.txt_calories_tab:
                binding.vf.setDisplayedChild(0);
                break;

            case R.id.txt_nutrients_tab:
                binding.vf.setDisplayedChild(1);
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

            case R.id.txt_log_calories:
                intent = new Intent(context, SearchFoodActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_log_nutrients:
                intent = new Intent(context, SearchFoodActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_show_log_calories:
                ll_calories_details.setVisibility(View.VISIBLE);
                rl_calories_bar.setVisibility(View.GONE);
                break;

            case R.id.txt_show_log_nutrients:
                ll_nutrients_details.setVisibility(View.VISIBLE);
                rl_nutrients_bar.setVisibility(View.GONE);
                break;

            case R.id.txt_show_chart_calories:
                ll_calories_details.setVisibility(View.GONE);
                rl_calories_bar.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_show_chart_nutrients:
                ll_nutrients_details.setVisibility(View.GONE);
                rl_nutrients_bar.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setCaloriesBarChart() {

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
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.animateXY(2000, 2000);

        XAxis xLabels = barChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getLegend().setEnabled(false);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("S");
        xAxisLabel.add("M");
        xAxisLabel.add("T");
        xAxisLabel.add("W");
        xAxisLabel.add("T");
        xAxisLabel.add("F");
        xAxisLabel.add("S");

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
                new float[]{15.0f, 15.0f}));

        values.add(new BarEntry(
                1,
                new float[]{15.0f, 15.0f}));

        values.add(new BarEntry(
                2,
                new float[]{15.0f, 15.0f}));

        values.add(new BarEntry(
                3,
                new float[]{15.0f, 15.0f}));

        values.add(new BarEntry(
                4,
                new float[]{15.0f, 15.0f}));

        values.add(new BarEntry(
                5,
                new float[]{15.0f, 15.0f}));

        values.add(new BarEntry(
                6,
                new float[]{15.0f, 15.0f}));

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
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Calories", "Burned Calories"});

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
        colors.add(getResources().getColor(R.color.light_blue_text));
        colors.add(getResources().getColor(R.color.purple));
        colors.add(getResources().getColor(R.color.empty_chart));

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[2];

//        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        colors[0] = getResources().getColor(R.color.light_blue_text);
        colors[1] = getResources().getColor(R.color.purple);

        return colors;
    }
}
