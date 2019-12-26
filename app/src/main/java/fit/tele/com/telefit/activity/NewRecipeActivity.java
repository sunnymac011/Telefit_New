package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.RecipeListAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityNewRecipeBinding;
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

public class NewRecipeActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener {

    ActivityNewRecipeBinding binding;
    private ArrayList<ChompProductBean> chompProductBeans = new ArrayList<>();
    private RecipeListAdapter recipeListAdapter1;
    private NewRecipeBean newRecipeBean;
    private ItemTouchHelper mItemTouchHelper;
    private double totCal = 0;
    DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public int getLayoutResId() {
        return R.layout.activity_new_recipe;
    }

    @Override
    public void init() {
        binding = (ActivityNewRecipeBinding) getBindingObj();

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

        if (preferences.getRecipeNameDataPref() != null)
            binding.inputRecipeName.setText(preferences.getRecipeNameDataPref());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvRecipe.setLayoutManager(linearLayoutManager);
        if (recipeListAdapter1 == null) {
            recipeListAdapter1 = new RecipeListAdapter(context, this, new RecipeListAdapter.ClickListener() {
                @Override
                public void onClick(ChompProductBean chompProductBean) {
                    preferences.saveRecipeNameData(""+binding.inputRecipeName.getText());

                    preferences.saveRecipeData(chompProductBeans);

                    Intent intent = new Intent(context, AddFoodActivity.class);
                    intent.putExtra("from","NewRecipeActivity");
                    intent.putExtra("SelectedItems",chompProductBean);
                    context.startActivity(intent);
                }
            });
        }
        binding.rvRecipe.setAdapter(recipeListAdapter1);
        recipeListAdapter1.clearAll();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recipeListAdapter1);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rvRecipe);

        if (preferences.getRecipeDataPref() != null) {
            Gson gson = new Gson();
            chompProductBeans = gson.fromJson(preferences.getRecipeDataPref(), new TypeToken<ArrayList<ChompProductBean>>(){}.getType());
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
                if (TextUtils.isEmpty(binding.inputRecipeName.getText()))
                {
                    CommonUtils.toast(context, "Please give Recipe Name");
                    return;
                }
                preferences.saveRecipeNameData(""+binding.inputRecipeName.getText());

                preferences.saveRecipeData(chompProductBeans);

                intent = new Intent(context, SearchFoodActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                preferences.cleanRecipedata();
                if (TextUtils.isEmpty(binding.inputRecipeName.getText()))
                    CommonUtils.toast(context, "Please give Recipe Name");
                else {
                    if (recipeListAdapter1.getAllData().size() > 0)
                    {
                        newRecipeBean = new NewRecipeBean();
                        newRecipeBean.setRecipeName(binding.inputRecipeName.getText().toString().trim());
                        newRecipeBean.setFoodType(binding.inputRecipeName.getText().toString().trim());
                        newRecipeBean.setIsRacipeMeal("1");
                        newRecipeBean.setRecipe_id("0");

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

                        Log.e("newRecipeBean",""+newRecipeBean.toString());
                        callCreateRecipeApi();
                    }
                    else
                        CommonUtils.toast(context, "Please add food");
                }

                break;
        }
    }

    private void callCreateRecipeApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<NewRecipeBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).createRecipeApi(newRecipeBean);
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
                            Log.e("callCreateRecipeApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<NewRecipeBean> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getStatus() == 1) {
                                preferences.cleanRecipedata();
                                preferences.cleanRecipeNamedata();
                                Intent intent = new Intent(context, SearchFoodActivity.class);
                                intent.putExtra("tab",4);
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

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewRecipeActivity.this, FoodCalNutActivity.class));
        this.overridePendingTransition(0, 0);
    }
}
