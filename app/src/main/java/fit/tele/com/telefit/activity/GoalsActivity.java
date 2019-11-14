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
    private LimitLine ll1,ll2,ll3,ll4,ll5;

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
        final String[] months = new String[]{"1st week", "2nd week", "3rd week", "4th week", "5th week"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
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
        if (ll2 != null)
            leftAxis.addLimitLine(ll2);
        if (ll3 != null)
            leftAxis.addLimitLine(ll3);
        if (ll4 != null)
            leftAxis.addLimitLine(ll4);
        if (ll5 != null)
            leftAxis.addLimitLine(ll5);
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
            binding.lineChart.animateX(2000);
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
                                if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getWater() != null &&
                                        !TextUtils.isEmpty(goalBarBean.getOne_week().getWater()))
                                {
                                    ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getGoalWater()), "1st Goal");
                                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                                    ll1.setLineWidth(2f);
                                    entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getWater())));
                                }
                                else
                                    entries.add(new Entry(0,0));
                                if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getWater() != null &&
                                        !TextUtils.isEmpty(goalBarBean.getTwo_week().getWater()))
                                {
                                    ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getGoalWater()), "2nd Goal");
                                    ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                                    ll2.setLineWidth(2f);
                                    entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getWater())));
                                }
                                else
                                    entries.add(new Entry(1,0));
                                if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getWater() != null &&
                                        !TextUtils.isEmpty(goalBarBean.getThree_week().getWater()))
                                {
                                    ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getGoalWater()), "3rd Goal");
                                    ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                                    ll3.setLineWidth(2f);
                                    entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getWater())));
                                }
                                else
                                    entries.add(new Entry(2,0));
                                if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getWater() != null &&
                                        !TextUtils.isEmpty(goalBarBean.getFour_week().getWater()))
                                {
                                    ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getGoalWater()), "4th Goal");
                                    ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                                    ll4.setLineWidth(2f);
                                    entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getWater())));
                                }
                                else
                                    entries.add(new Entry(3,0));
                                if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getWater() != null &&
                                        !TextUtils.isEmpty(goalBarBean.getFive_week().getWater()))
                                {
                                    ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getGoalWater()), "5th Goal");
                                    ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                                    ll5.setLineWidth(2f);
                                    entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getWater())));
                                }
                                else
                                    entries.add(new Entry(4,0));
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
        if (selectedNutrition.equalsIgnoreCase("Protein")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getMealProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getMealProtein())
                    && goalBarBean.getOne_week().getProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getProtein()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getProtein()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getMealProtein())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getMealProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getMealProtein())
                    && goalBarBean.getTwo_week().getProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getProtein()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getProtein()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getMealProtein())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getMealProtein())
                    && goalBarBean.getThree_week().getProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getProtein()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getProtein()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getMealProtein())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getMealProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getMealProtein())
                    && goalBarBean.getFour_week().getProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getProtein()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getProtein()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getMealProtein())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getMealProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getMealProtein())
                    && goalBarBean.getFive_week().getProtein() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getProtein()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getProtein()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getMealProtein())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Carbs")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getMealCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getMealCarbs())
                    && goalBarBean.getOne_week().getCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getCarbs()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getCarbs()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getMealCarbs())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getMealCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getMealCarbs())
                    && goalBarBean.getTwo_week().getCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getCarbs()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getCarbs()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getMealCarbs())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getMealCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getMealCarbs())
                    && goalBarBean.getThree_week().getCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getCarbs()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getCarbs()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getMealCarbs())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getMealCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getMealCarbs())
                    && goalBarBean.getFour_week().getCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getCarbs()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getCarbs()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getMealCarbs())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getMealCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getMealCarbs())
                    && goalBarBean.getFive_week().getCarbs() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getCarbs()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getCarbs()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getMealCarbs())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Fat")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getMealFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getMealFat())
                    && goalBarBean.getOne_week().getFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getFat()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getFat()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getMealFat())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getMealFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getMealFat())
                    && goalBarBean.getTwo_week().getFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getFat()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getFat()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getMealFat())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getMealFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getMealFat())
                    && goalBarBean.getThree_week().getFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getFat()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getFat()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getMealFat())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getMealFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getMealFat())
                    && goalBarBean.getFour_week().getFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getFat()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getFat()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getMealFat())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getMealFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getMealFat())
                    && goalBarBean.getFive_week().getFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getFat()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getFat()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getMealFat())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Cholesterol")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getMealCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getMealCholesterol())
                    && goalBarBean.getOne_week().getCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getCholesterol()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getCholesterol()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getMealCholesterol())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getMealCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getMealCholesterol())
                    && goalBarBean.getTwo_week().getCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getCholesterol()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getCholesterol()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getMealCholesterol())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getMealCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getMealCholesterol())
                    && goalBarBean.getThree_week().getCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getCholesterol()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getCholesterol()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getMealCholesterol())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getMealCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getMealCholesterol())
                    && goalBarBean.getFour_week().getCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getCholesterol()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getCholesterol()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getMealCholesterol())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getMealCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getMealCholesterol())
                    && goalBarBean.getFive_week().getCholesterol() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getCholesterol()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getCholesterol()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getMealCholesterol())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Fiber")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getMealFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getMealFiber())
                    && goalBarBean.getOne_week().getFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getFiber()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getFiber()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getMealFiber())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getMealFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getMealFiber())
                    && goalBarBean.getTwo_week().getFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getFiber()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getFiber()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getMealFiber())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getMealFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getMealFiber())
                    && goalBarBean.getThree_week().getFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getFiber()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getFiber()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getMealFiber())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getMealFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getMealFiber())
                    && goalBarBean.getFour_week().getFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getFiber()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getFiber()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getMealFiber())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getMealFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getMealFiber())
                    && goalBarBean.getFive_week().getFiber() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getFiber()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getFiber()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getMealFiber())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Weight")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getWeight())
                    && goalBarBean.getOne_week().getGoalWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getGoalWeight()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getGoalWeight()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getWeight())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getWeight())
                    && goalBarBean.getTwo_week().getGoalWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getGoalWeight()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getGoalWeight()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getWeight())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getWeight())
                    && goalBarBean.getThree_week().getGoalWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getGoalWeight()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getGoalWeight()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getWeight())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getWeight())
                    && goalBarBean.getFour_week().getGoalWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getGoalWeight()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getGoalWeight()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getWeight())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getWeight())
                    && goalBarBean.getFive_week().getGoalWeight() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getGoalWeight()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getGoalWeight()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getWeight())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Body Fat")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getBodyFat())
                    && goalBarBean.getOne_week().getGoalBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getGoalBodyFat()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getGoalBodyFat()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getBodyFat())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getBodyFat())
                    && goalBarBean.getTwo_week().getGoalBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getGoalBodyFat()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getGoalBodyFat()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getBodyFat())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getBodyFat())
                    && goalBarBean.getThree_week().getGoalBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getGoalBodyFat()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getGoalBodyFat()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getBodyFat())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getBodyFat())
                    && goalBarBean.getFour_week().getGoalBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getGoalBodyFat()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getGoalBodyFat()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getBodyFat())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getBodyFat())
                    && goalBarBean.getFive_week().getGoalBodyFat() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getGoalBodyFat()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getGoalBodyFat()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getBodyFat())));
            }
            else
                entries.add(new Entry(4,0));
        }
        if (selectedNutrition.equalsIgnoreCase("Water")) {
            if (goalBarBean.getOne_week() != null && goalBarBean.getOne_week().getWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getWater())
                    && goalBarBean.getOne_week().getGoalWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getOne_week().getGoalWater()))
            {
                ll1 = new LimitLine(Float.parseFloat(goalBarBean.getOne_week().getGoalWater()), "1st Goal");
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setLineWidth(2f);
                entries.add(new Entry(0, Float.parseFloat(goalBarBean.getOne_week().getWater())));
            }
            else
                entries.add(new Entry(0,0));
            if (goalBarBean.getTwo_week() != null && goalBarBean.getTwo_week().getWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getWater())
                    && goalBarBean.getTwo_week().getGoalWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getTwo_week().getGoalWater()))
            {
                ll2 = new LimitLine(Float.parseFloat(goalBarBean.getTwo_week().getGoalWater()), "2nd Goal");
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll2.setLineWidth(2f);
                entries.add(new Entry(1, Float.parseFloat(goalBarBean.getTwo_week().getWater())));
            }
            else
                entries.add(new Entry(1,0));
            if (goalBarBean.getThree_week() != null && goalBarBean.getThree_week().getWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getWater())
                    && goalBarBean.getThree_week().getGoalWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getThree_week().getGoalWater()))
            {
                ll3 = new LimitLine(Float.parseFloat(goalBarBean.getThree_week().getGoalWater()), "3rd Goal");
                ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll3.setLineWidth(2f);
                entries.add(new Entry(2, Float.parseFloat(goalBarBean.getThree_week().getWater())));
            }
            else
                entries.add(new Entry(2,0));
            if (goalBarBean.getFour_week() != null && goalBarBean.getFour_week().getWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getWater())
                    && goalBarBean.getFour_week().getGoalWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFour_week().getGoalWater()))
            {
                ll4 = new LimitLine(Float.parseFloat(goalBarBean.getFour_week().getGoalWater()), "4th Goal");
                ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll4.setLineWidth(2f);
                entries.add(new Entry(3, Float.parseFloat(goalBarBean.getFour_week().getWater())));
            }
            else
                entries.add(new Entry(3,0));
            if (goalBarBean.getFive_week() != null && goalBarBean.getFive_week().getWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getWater())
                    && goalBarBean.getFive_week().getGoalWater() != null &&
                    !TextUtils.isEmpty(goalBarBean.getFive_week().getGoalWater()))
            {
                ll5 = new LimitLine(Float.parseFloat(goalBarBean.getFive_week().getGoalWater()), "5th Goal");
                ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll5.setLineWidth(2f);
                entries.add(new Entry(4, Float.parseFloat(goalBarBean.getFive_week().getWater())));
            }
            else
                entries.add(new Entry(4,0));
        }
        renderData();
    }
}
