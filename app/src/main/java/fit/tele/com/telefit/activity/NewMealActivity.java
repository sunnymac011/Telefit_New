package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.RecipeListAdapter;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCreateMealBinding;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;

public class NewMealActivity extends BaseActivity implements View.OnClickListener {

    ActivityCreateMealBinding binding;
    private ArrayList<ChompProductBean> chompProductBeans;
    private RecipeListAdapter recipeListAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_create_meal;
    }

    @Override
    public void init() {
        binding = (ActivityCreateMealBinding) getBindingObj();
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
        binding.llAddFood.setOnClickListener(this);
        binding.txtAdd.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvMeal.setLayoutManager(linearLayoutManager);
        if (recipeListAdapter == null) {
            recipeListAdapter = new RecipeListAdapter(context);
        }
        binding.rvMeal.setAdapter(recipeListAdapter);
        recipeListAdapter.clearAll();
        if (preferences.getMealDataPref() != null) {
            Gson gson = new Gson();
            chompProductBeans = gson.fromJson(preferences.getMealDataPref(), new TypeToken<ArrayList<ChompProductBean>>(){}.getType());
            if (chompProductBeans != null && chompProductBeans.size() > 0)
            {
                recipeListAdapter.addAllList(chompProductBeans);
                binding.llMealEmpty.setVisibility(View.GONE);
                binding.llMealDesign.setVisibility(View.VISIBLE);
            }
            else {
                binding.llMealEmpty.setVisibility(View.VISIBLE);
                binding.llMealDesign.setVisibility(View.GONE);
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

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_add_food:
                intent = new Intent(context, SearchFoodActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                preferences.cleanMealdata();
                break;
        }
    }
}
