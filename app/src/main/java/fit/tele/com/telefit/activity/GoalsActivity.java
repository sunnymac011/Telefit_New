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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
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

public class GoalsActivity extends BaseActivity implements View.OnClickListener {

    ActivityGoalsBinding binding;
    private DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    private GoalCategoryAdapter goalCategoryAdapter;
    private ArrayList<String> goalList = new ArrayList<>();
    private ArrayList<Entry> entries = new ArrayList<>();
    private GoalBarBean goalBarBean;
    private LimitLine ll1;
    private String[] months;

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

        callGetGoalApi(format1.format(calendar.getTime()));
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
        }
    }

    public void renderData() {
        binding.lineChart.clear();
        binding.lineChart.setTouchEnabled(false);
        binding.lineChart.setPinchZoom(false);
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getLegend().setEnabled(false);
        XAxis xAxis = binding.lineChart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Customizing x axis value

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (months.length == 1)
                    return months[0];
                else
                    return months[(int) value];
            }
        };
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = binding.lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();

        if (ll1 != null)
            leftAxis.addLimitLine(ll1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);

        binding.lineChart.getAxisRight().setEnabled(false);
        setData();
    }

    private void setData() {

//        ArrayList<Entry> values = new ArrayList<>();
//        values.add(new Entry(0, 50));
//        values.add(new Entry(1, 100));
//        values.add(new Entry(2, 80));
//        values.add(new Entry(3, 120));
//        values.add(new Entry(4, 110));

        LineDataSet set1;
        if (binding.lineChart.getData() != null &&
                binding.lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) binding.lineChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            binding.lineChart.getData().notifyDataChanged();
            binding.lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(entries, "");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(getResources().getColor(R.color.main_color));
            set1.setCircleColor(getResources().getColor(R.color.light_blue));
            set1.setLineWidth(3f);
            set1.setCircleRadius(6f);
            set1.setDrawCircleHole(false);
            set1.setDrawFilled(false);
            set1.setFormLineWidth(1f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            binding.lineChart.setData(data);
            binding.lineChart.animateX(1500);
            binding.lineChart.invalidate();
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
                                entries.clear();
                                if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().size() > 0)
                                {
                                    if (goalBarBean.getGoalWater() != null) {
                                        ll1 = new LimitLine(Float.parseFloat(goalBarBean.getGoalWater()), "Goal");
                                        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                                        ll1.setLineWidth(2f);
                                    }
                                    months = new String[goalBarBean.getGoalDetails().size()];
                                    for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                                            DateFormat format2=new SimpleDateFormat("dd");
                                            Log.e("date",""+format2.format(date));
                                            months[i] = (String) format2.format(date);

                                        } catch (Exception e) {
                                            Log.e("date Exception",""+e.getMessage());
                                        }
                                        if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getWater() != null &&
                                                !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getWater()))
                                            entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getWater())));
                                        else
                                            entries.add(new Entry(i, 0));
                                    }
                                }
                                renderData();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void setLineChartData(String selectedNutrition){
        entries.clear();
        if (goalBarBean != null && goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().size() > 0)
        {
            if (selectedNutrition.equalsIgnoreCase("Protein")) {
                if (goalBarBean.getProtein() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getProtein()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);
                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getMealProtein() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getMealProtein()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getMealProtein())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Carbs")) {
                if (goalBarBean.getCarbs() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getCarbs()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);

                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getMealCarbs() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getMealCarbs()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getMealCarbs())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Fat")) {
                if (goalBarBean.getFat() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getFat()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);

                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getMealFat() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getMealFat()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getMealFat())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Cholesterol")) {
                if (goalBarBean.getCholesterol() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getCholesterol()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);

                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getMealCholesterol() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getMealCholesterol()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getMealCholesterol())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Fiber")) {
                if (goalBarBean.getFiber() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getFiber()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);

                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getMealFiber() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getMealFiber()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getMealFiber())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Weight")) {
                if (goalBarBean.getGoalWeight() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getGoalWeight()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);
                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getWeight() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getWeight()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getWeight())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Body Fat")) {
                if (goalBarBean.getGoalBodyFat() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getGoalBodyFat()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);

                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getBodyFat() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getBodyFat()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getBodyFat())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            if (selectedNutrition.equalsIgnoreCase("Water")) {
                if (goalBarBean.getGoalWater() != null) {
                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getGoalWater()), "Goal");
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setLineWidth(2f);
                }

                months = new String[goalBarBean.getGoalDetails().size()];
                for (int i=0;i<goalBarBean.getGoalDetails().size();i++) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(goalBarBean.getGoalDetails().get(i).getGoalDate());
                        DateFormat format2=new SimpleDateFormat("dd");
                        Log.e("date",""+format2.format(date));
                        months[i] = (String) format2.format(date);
                    } catch (Exception e) {
                        Log.e("date Exception",""+e.getMessage());
                    }
                    if (goalBarBean.getGoalDetails() != null && goalBarBean.getGoalDetails().get(i).getWater() != null &&
                            !TextUtils.isEmpty(goalBarBean.getGoalDetails().get(i).getWater()))
                        entries.add(new Entry(i, Float.parseFloat(goalBarBean.getGoalDetails().get(i).getWater())));
                    else
                        entries.add(new Entry(i, 0));
                }
            }
            renderData();
        }
    }
}
