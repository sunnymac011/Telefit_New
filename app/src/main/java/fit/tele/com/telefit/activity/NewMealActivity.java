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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.RecipeListAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCreateMealBinding;
import fit.tele.com.telefit.helper.OnStartDragListener;
import fit.tele.com.telefit.helper.SimpleItemTouchHelperCallback;
import fit.tele.com.telefit.modelBean.MealCategoryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.NewRecipeBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewMealActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener {

    ActivityCreateMealBinding binding;
    private ArrayList<ChompProductBean> chompProductBeans = new ArrayList<>();
    private RecipeListAdapter recipeListAdapter1;
    private NewRecipeBean newRecipeBean;
    private ItemTouchHelper mItemTouchHelper;
    private String recipeName = "", recipeID = "", foodCatID = "", fooddate = "";
    private double totCal = 0;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<MealCategoryBean> mealCategoryBeans = new ArrayList<>();
    DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public int getLayoutResId() {
        return R.layout.activity_create_meal;
    }

    @Override
    public void init() {
        binding = (ActivityCreateMealBinding) getBindingObj();

        if (getIntent().hasExtra("recipeName"))
            recipeName = getIntent().getStringExtra("recipeName");
        if (getIntent().hasExtra("recipeID"))
            recipeID = getIntent().getStringExtra("recipeID");
        if (getIntent().hasExtra("foodCatID"))
            foodCatID = getIntent().getStringExtra("foodCatID");
        if (getIntent().hasExtra("fooddate"))
            fooddate = getIntent().getStringExtra("fooddate");

        callGetMealCatApi();
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
        binding.llSocial.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llAddFood.setOnClickListener(this);
        binding.txtAdd.setOnClickListener(this);

        foodCatID = preferences.getMealIDDataPref();
        if (preferences.isUpdateMeal() != null && !TextUtils.isEmpty(preferences.isUpdateMeal()))
            recipeID = preferences.isUpdateMeal();
        if (preferences.getMealDateDataPref() != null && !TextUtils.isEmpty(preferences.getMealDateDataPref()))
            fooddate = preferences.getMealDateDataPref();

        if (fooddate == null || TextUtils.isEmpty(fooddate))
        {
            Calendar calendar = Calendar.getInstance();
            fooddate = format1.format(calendar.getTime());
        }
        binding.txtMealDate.setText(fooddate);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvMeal.setLayoutManager(linearLayoutManager);
        if (recipeListAdapter1 == null) {
            recipeListAdapter1 = new RecipeListAdapter(context, this, new RecipeListAdapter.ClickListener() {
                @Override
                public void onClick(ChompProductBean chompProductBean) {
                    preferences.saveMealNameData(""+binding.spiMeal.getSelectedItemPosition());
                    if (!TextUtils.isEmpty(binding.txtMealDate.getText()))
                        preferences.saveMealDateData(binding.txtMealDate.getText().toString());

                    preferences.saveMealData(chompProductBeans);

                    Intent intent = new Intent(context, AddFoodActivity.class);
                    intent.putExtra("from","NewMealActivity");
                    intent.putExtra("SelectedItems",chompProductBean);
                    context.startActivity(intent);
                }
            });
        }
        binding.rvMeal.setAdapter(recipeListAdapter1);
        recipeListAdapter1.clearAll();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recipeListAdapter1);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rvMeal);

        if (preferences.getMealDataPref() != null) {
            Gson gson = new Gson();
            chompProductBeans = gson.fromJson(preferences.getMealDataPref(), new TypeToken<ArrayList<ChompProductBean>>(){}.getType());
            recipeListAdapter1.addAllList(chompProductBeans);

            if (chompProductBeans != null && chompProductBeans.size() > 0)
            {
                for (int i=0;i<chompProductBeans.size();i++)
                    totCal = totCal + Double.parseDouble(chompProductBeans.get(i).getTotalCalories());

                double convertedCal = totCal/4.184;
                binding.txtCalories.setText(String.format("%.2f", convertedCal)+" Calories per serving");
            }
        }
        else
        {
            if (recipeID!=null && !TextUtils.isEmpty(recipeID))
            {
                preferences.saveUpdateMeal(recipeID);
                callGetMealApi(foodCatID, recipeID);
            }
        }
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

            case R.id.ll_add_food:
                preferences.saveMealNameData(""+binding.spiMeal.getSelectedItemPosition());
                if (!TextUtils.isEmpty(binding.txtMealDate.getText()))
                    preferences.saveMealDateData(binding.txtMealDate.getText().toString());

                preferences.saveMealData(chompProductBeans);

                intent = new Intent(context, SearchFoodActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                preferences.cleanMealdata();
                if (TextUtils.isEmpty(binding.txtMealDate.getText()))
                    CommonUtils.toast(context, "Please select Date!");
                else {
                    if (recipeListAdapter1.getAllData().size() > 0)
                    {
                        if (foodCatID == null)
                            foodCatID = "0";

                        newRecipeBean = new NewRecipeBean();
                        newRecipeBean.setRecipeName(binding.spiMeal.getSelectedItem().toString());
                        newRecipeBean.setFoodType(binding.spiMeal.getSelectedItem().toString());
                        newRecipeBean.setMealDate(binding.txtMealDate.getText().toString());
                        newRecipeBean.setIsRacipeMeal("2");
                        newRecipeBean.setRecipe_id(mealCategoryBeans.get(binding.spiMeal.getSelectedItemPosition()).getId());

                        double intTotCal = 0;
                        if (recipeListAdapter1.getAllData() != null && recipeListAdapter1.getAllData().size() > 0)
                        {
                            for (int i=0;i<recipeListAdapter1.getAllData().size();i++)
                            {
                                intTotCal = intTotCal + Double.parseDouble(recipeListAdapter1.getAllData().get(i).getTotalCalories());
                                recipeListAdapter1.getAllData().get(i).setUpc("");
                                recipeListAdapter1.getAllData().get(i).setManufacturer("");
                            }
                        }
                        newRecipeBean.setRacipeCalories(""+intTotCal);
                        newRecipeBean.setFood(recipeListAdapter1.getAllData());

                        if (newRecipeBean.getRecipe_id() != null)
                        {
                            if (recipeID != null && !TextUtils.isEmpty(recipeID))
                            {
                                newRecipeBean.setMealsId(recipeID);
                                callUpdateApi();
                            }
                            else
                                callCreateApi();
                        }
                    }
                    else
                        CommonUtils.toast(context, "Please add food");
                }
                break;
        }
    }

    private void callCreateApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<NewRecipeBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).createMealApi(newRecipeBean);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<NewRecipeBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCreateMealApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<NewRecipeBean> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getStatus() == 1) {
                                preferences.cleanMealNamedata();
                                preferences.cleanMealDatedata();
                                preferences.cleanMealdata();
                                preferences.cleanMealIDdata();
                                preferences.cleanUpdateMeal();
                                Intent intent = new Intent(context, FoodCalNutActivity.class);
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

    private void callUpdateApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<NewRecipeBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).updateRecipeApi(newRecipeBean);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<NewRecipeBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callUpdateApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<NewRecipeBean> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getStatus() == 1) {
                                preferences.cleanMealNamedata();
                                preferences.cleanMealDatedata();
                                preferences.cleanMealdata();
                                preferences.cleanMealIDdata();
                                Intent intent = new Intent(context, FoodCalNutActivity.class);
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

    private void callGetMealCatApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", "2");

            Observable<ModelBean<ArrayList<MealCategoryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getMealCatApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<MealCategoryBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetMealApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<MealCategoryBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                if (apiFoodBean.getResult().size()>0) {
                                    mealCategoryBeans = apiFoodBean.getResult();
                                    arrayList.clear();
                                    for (int i=0;i<apiFoodBean.getResult().size();i++) {
                                        arrayList.add(apiFoodBean.getResult().get(i).getFoodType());
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_custom_spinner, arrayList);
                                    binding.spiMeal.setAdapter(adapter);

                                    if (!TextUtils.isEmpty(recipeName))
                                    {
                                        for (int j=0;j<arrayList.size();j++) {
                                            if (arrayList.get(j).equalsIgnoreCase(recipeName))
                                            {
                                                binding.spiMeal.setEnabled(false);
                                                binding.spiMeal.setSelection(j);

                                                return;
                                            }
                                        }
                                    }

                                    if (preferences.getMealNameDataPref() != null && !TextUtils.isEmpty(preferences.getMealNameDataPref()))
                                    {
                                        binding.spiMeal.setSelection(Integer.parseInt(preferences.getMealNameDataPref()));
                                        binding.spiMeal.setEnabled(false);
                                    }
                                }
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callGetMealApi(String newSnack, String newRecipe) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("meals_id", newRecipe);
            map.put("food_cat_id", newSnack);
            map.put("is_racipe_meal", "2");

            Observable<ModelBean<NewRecipeBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getMealApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<NewRecipeBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetMealApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<NewRecipeBean> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                chompProductBeans = apiFoodBean.getResult().getFood();
                                recipeListAdapter1.addAllList(chompProductBeans);

                                if (chompProductBeans != null && chompProductBeans.size() > 0)
                                {
                                    for (int i=0;i<chompProductBeans.size();i++)
                                        totCal = totCal + Double.parseDouble(chompProductBeans.get(i).getTotalCalories());

                                    double convertedCal = totCal/4.184;
                                    binding.txtCalories.setText(String.format("%.2f", convertedCal)+" Calories per serving");
                                }
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
