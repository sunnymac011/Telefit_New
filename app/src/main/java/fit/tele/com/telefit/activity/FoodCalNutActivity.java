package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.FoodCategoryAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityFoodCalNutBinding;
import fit.tele.com.telefit.modelBean.FoodCategoryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FoodCalNutActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ActivityFoodCalNutBinding binding;
    private FoodCategoryAdapter foodCategoryAdapter;
    private String strSelectedDate = "0",strCurrentDate = "", strSu, strMo,strTu,strWe,strTh,strFr,strSa;
    private DatePickerDialog dpd;
    private DateFormat apiformat = new SimpleDateFormat("yyyy/MM/dd");
    private DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat format = new SimpleDateFormat("dd");
    private DateFormat format2 = new SimpleDateFormat("yyyy/MM");
    private double totalCal = 0, convertedTotalCal = 0,budgetCal = 0,burnCal = 0,netCal = 0,underCal = 0,weektotalCal = 0,convertedWeektotalCal = 0,weekbudgetCal = 0,weekburnCal = 0
            ,weeknetCal = 0,weekunderCal = 0, bmr = 0, weight=0, height=0, age =0, tdee = 0;
    private Calendar calendar;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_food_cal_nut;
    }

    @Override
    public void init() {
        binding = (ActivityFoodCalNutBinding) getBindingObj();

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        binding.llAddSnack.setOnClickListener(this);

        binding.llSunday.setOnClickListener(this);
        binding.llMonday.setOnClickListener(this);
        binding.llTuesday.setOnClickListener(this);
        binding.llWednesday.setOnClickListener(this);
        binding.llThursday.setOnClickListener(this);
        binding.llFriday.setOnClickListener(this);
        binding.llSaturday.setOnClickListener(this);
        binding.txtDate.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvFoodRecipe.setLayoutManager(linearLayoutManager);

        setData();
    }

    private void setData() {
        burnCal = Float.parseFloat(preferences.getBurnedCaloriesPref());
        calendar = Calendar.getInstance();
        binding.txtDate.setText(format1.format(calendar.getTime()));
        strSelectedDate = format.format(calendar.getTime());
        strCurrentDate = format2.format(calendar.getTime());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        setDates();

        if (foodCategoryAdapter == null)
        {
            foodCategoryAdapter = new FoodCategoryAdapter(context, new FoodCategoryAdapter.ClickListener() {
                @Override
                public void onClick(FoodCategoryBean categoryBean, boolean isBarcode) {
                    if (isBarcode) {
                        Intent intent = new Intent(FoodCalNutActivity.this, QRCodeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        preferences.cleanRecipedata();
                        preferences.cleanMealNamedata();
                        preferences.cleanMealdata();
                        preferences.cleanMealDatedata();
                        preferences.cleanUpdateMeal();
                        preferences.saveMealIDData(categoryBean.getId());
                        Intent intent = new Intent(FoodCalNutActivity.this, NewMealActivity.class);
                        intent.putExtra("recipeName",categoryBean.getFoodType());
                        intent.putExtra("foodCatID",categoryBean.getId());
                        try {
                            Log.e("strSelectedDate",""+strSelectedDate);
                            Date newDate = apiformat.parse(strSelectedDate);
                            intent.putExtra("fooddate",format1.format(newDate));
                            Log.e("strSelectedDate",""+format1.format(newDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (categoryBean.getRecipeBeans() != null) {
                            if (categoryBean.getRecipeBeans().get(0) != null)
                                intent.putExtra("recipeID",categoryBean.getRecipeBeans().get(0).getId());
                        }

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }

                @Override
                public void onDeletClick(String foodCatId, FoodCategoryBean categoryBean) {
                    callDeleteCatApi(foodCatId);
                }
            });
        }
        binding.rvFoodRecipe.setAdapter(foodCategoryAdapter);
        foodCategoryAdapter.clearAll();

        if (strSelectedDate.length() < 3)
            strSelectedDate = strCurrentDate+"/"+strSelectedDate;

        calculateBudget();
        callMealDateApi(strSelectedDate);
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
            case R.id.ll_add_snack:
                int newSnack = (foodCategoryAdapter.getItemCount()-5)+3;
                callAddCatApi("Snack"+newSnack);
                break;
            case R.id.ll_sunday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                strSelectedDate = strSu;
                callMealDateApi(strSu);
                break;
            case R.id.ll_monday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                strSelectedDate = strMo;
                callMealDateApi(strMo);
                break;
            case R.id.ll_tuesday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                strSelectedDate = strTu;
                callMealDateApi(strTu);
                break;
            case R.id.ll_wednesday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                strSelectedDate = strWe;
                callMealDateApi(strWe);
                break;
            case R.id.ll_thursday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                strSelectedDate = strTh;
                callMealDateApi(strTh);
                break;
            case R.id.ll_friday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                strSelectedDate = strFr;
                callMealDateApi(strFr);
                break;
            case R.id.ll_saturday:
                binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
                binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle);
                strSelectedDate = strSa;
                callMealDateApi(strSa);
                break;

            case R.id.txt_date:
                datePicker();
                break;
        }
    }

    private void callMealDateApi(String strDate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", "2");
            map.put("meals_date", strDate);
//            try {
//                Date newDate = apiformat.parse(strDate);
//                map.put("meals_date", strDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            Observable<ModelBean<ArrayList<FoodCategoryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getMealByDateApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<FoodCategoryBean>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callFoodCatApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<FoodCategoryBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                foodCategoryAdapter.clearAll();
                                foodCategoryAdapter.addAllList(apiFoodBean.getResult());

                                totalCal = 0;
                                for (int i=0;i<apiFoodBean.getResult().size();i++) {
                                    if (apiFoodBean.getResult().get(i).getRecipeBeans() != null && apiFoodBean.getResult().get(i).getRecipeBeans().get(0) != null &&
                                            apiFoodBean.getResult().get(i).getRecipeBeans().get(0).getRacipeCalories() != null)
                                        totalCal = totalCal+Double.parseDouble(apiFoodBean.getResult().get(i).getRecipeBeans().get(0).getRacipeCalories());
                                }
                                convertedTotalCal = totalCal/4.184;
                                binding.txtFood.setText(""+String.format("%.2f", convertedTotalCal));
                                binding.txtBudget.setText(""+String.format("%.2f", budgetCal));
                                binding.txtExercise.setText(""+String.format("%.2f", burnCal));
                                netCal = convertedTotalCal - burnCal;
                                underCal = budgetCal - netCal;
                                binding.txtNet.setText(""+String.format("%.2f", netCal));
                                binding.txtUnder.setText(""+String.format("%.2f", Math.abs(underCal)));
                                if (underCal < 0)
                                    binding.txtUnder.setTextColor(getResources().getColor(R.color.graph_red));
                                else
                                    binding.txtUnder.setTextColor(getResources().getColor(R.color.light_blue_text));
                                if (apiFoodBean.getResult().size() > 0)
                                {
                                    if (apiFoodBean.getResult().get(0).getTotalCaloriesWeek() != null)
                                        weektotalCal = Double.parseDouble(apiFoodBean.getResult().get(0).getTotalCaloriesWeek());
                                }

                                convertedWeektotalCal = weektotalCal/4.184;
                                weeknetCal = convertedWeektotalCal - weekburnCal;
                                weekunderCal = (budgetCal*7) - weeknetCal;
                                binding.txtWeekUnder.setText(""+String.format("%.2f", weekunderCal));

                                binding.pbCalorie.setMax((int) budgetCal);
                                binding.pbCalorie.setProgress((int) netCal);
                            }
                            else
                                CommonUtils.toast(context, ""+apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callAddCatApi(String strType) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("food_type", strType);
            map.put("is_racipe_meal", "2");

            Observable<ModelBean<ArrayList<FoodCategoryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getAddCatApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<FoodCategoryBean>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callAddCatApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<FoodCategoryBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                foodCategoryAdapter.clearAll();
                                foodCategoryAdapter.addAllList(apiFoodBean.getResult());

                                for (int i=0;i<apiFoodBean.getResult().size();i++) {
                                    if (apiFoodBean.getResult().get(i).getRecipeBeans() != null && apiFoodBean.getResult().get(i).getRecipeBeans().get(0) != null &&
                                            apiFoodBean.getResult().get(i).getRecipeBeans().get(0).getRacipeCalories() != null)
                                        totalCal = totalCal+Double.parseDouble(apiFoodBean.getResult().get(i).getRecipeBeans().get(0).getRacipeCalories());
                                }
                                convertedTotalCal = totalCal/4.184;
                                binding.txtFood.setText(""+String.format("%.2f", convertedTotalCal));
                                binding.txtBudget.setText(""+String.format("%.2f", budgetCal));
                                binding.txtExercise.setText(""+String.format("%.2f", burnCal));
                                netCal = convertedTotalCal - burnCal;
                                underCal = budgetCal - netCal;
                                binding.txtNet.setText(""+String.format("%.2f", netCal));
                                binding.txtUnder.setText(""+String.format("%.2f", Math.abs(underCal)));
                                if (underCal < 0)
                                    binding.txtUnder.setTextColor(getResources().getColor(R.color.graph_red));
                                else
                                    binding.txtUnder.setTextColor(getResources().getColor(R.color.light_blue_text));
                                binding.pbCalorie.setMax((int) budgetCal);
                                binding.pbCalorie.setProgress((int) netCal);

                                if (apiFoodBean.getResult().size() > 0)
                                {
                                    if (apiFoodBean.getResult().get(0).getTotalCaloriesWeek() != null)
                                    weektotalCal = Double.parseDouble(apiFoodBean.getResult().get(0).getTotalCaloriesWeek());
                                }
                                convertedWeektotalCal = weektotalCal/4.184;
                                weeknetCal = convertedWeektotalCal - weekburnCal;
                                weekunderCal = (budgetCal*7) - weeknetCal;
                                binding.txtWeekUnder.setText(""+String.format("%.2f", weekunderCal));
                            }
                            else
                                CommonUtils.toast(context, ""+apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callDeleteCatApi(String strID) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("food_cat_id", strID);
            map.put("is_racipe_meal", "2");

            Observable<ModelBean<ArrayList<FoodCategoryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getDeleteCatApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<FoodCategoryBean>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callDeleteCatApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<FoodCategoryBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                foodCategoryAdapter.clearAll();
                                foodCategoryAdapter.addAllList(apiFoodBean.getResult());

                                for (int i=0;i<apiFoodBean.getResult().size();i++) {
                                    if (apiFoodBean.getResult().get(i).getRecipeBeans() != null && apiFoodBean.getResult().get(i).getRecipeBeans().get(0) != null &&
                                            apiFoodBean.getResult().get(i).getRecipeBeans().get(0).getRacipeCalories() != null)
                                        totalCal = totalCal+Double.parseDouble(apiFoodBean.getResult().get(i).getRecipeBeans().get(0).getRacipeCalories());
                                }
                                convertedTotalCal = totalCal/4.184;
                                binding.txtFood.setText(""+String.format("%.2f", convertedTotalCal));
                                binding.txtBudget.setText(""+String.format("%.2f", budgetCal));
                                binding.txtExercise.setText(""+String.format("%.2f", burnCal));
                                netCal = convertedTotalCal - burnCal;
                                underCal = budgetCal - netCal;
                                binding.txtNet.setText(""+String.format("%.2f", netCal));
                                binding.txtUnder.setText(""+String.format("%.2f", Math.abs(underCal)));
                                if (underCal < 0)
                                    binding.txtUnder.setTextColor(getResources().getColor(R.color.graph_red));
                                else
                                    binding.txtUnder.setTextColor(getResources().getColor(R.color.light_blue_text));
                                binding.pbCalorie.setMax((int) budgetCal);
                                binding.pbCalorie.setProgress((int) netCal);

                                if (apiFoodBean.getResult().size() > 0)
                                {
                                    if (apiFoodBean.getResult().get(0).getTotalCaloriesWeek() != null)
                                        weektotalCal = Double.parseDouble(apiFoodBean.getResult().get(0).getTotalCaloriesWeek());
                                }
                                convertedWeektotalCal = weektotalCal/4.184;
                                weeknetCal = convertedWeektotalCal - weekburnCal;
                                weekunderCal = (budgetCal*7) - weeknetCal;
                                binding.txtWeekUnder.setText(""+String.format("%.2f", weekunderCal));
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

            dpd = DatePickerDialog.newInstance(FoodCalNutActivity.this, year, mm, dd);
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

        binding.txtDate.setText(date);
        strSelectedDate = date;
        try {
            Date newDate = format1.parse(strSelectedDate);
//            calendar.setTime(newDate);
//            setDates();
            strSelectedDate = apiformat.format(newDate);
            callMealDateApi(strSelectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void calculateBudget(){
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
            if (preferences.getUserDataPref().getMaintainWeight().equalsIgnoreCase("Lose 1.5 pound per week"))
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
    }

    private void setDates(){
        for (int i = 0; i < 7; i++)
        {
            if (i==0)
            {
                strSu = apiformat.format(calendar.getTime());
                binding.txtSundayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtSundayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            if (i==1)
            {
                strMo = apiformat.format(calendar.getTime());
                binding.txtMondayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtMondayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            if (i==2)
            {
                strTu = apiformat.format(calendar.getTime());
                binding.txtTuesdayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtTuesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            if (i==3)
            {
                strWe = apiformat.format(calendar.getTime());
                binding.txtWednesdayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtWednesdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            if (i==4)
            {
                strTh = apiformat.format(calendar.getTime());
                binding.txtThursdayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtThursdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            if (i==5)
            {
                strFr = apiformat.format(calendar.getTime());
                binding.txtFridayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtFridayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            if (i==6)
            {
                strSa = apiformat.format(calendar.getTime());
                binding.txtSaturdayDate.setText(format.format(calendar.getTime()));
                if (format.format(calendar.getTime()).equalsIgnoreCase(strSelectedDate))
                    binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle);
                else
                    binding.txtSaturdayDate.setBackgroundResource(R.drawable.calendar_circle_none);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
