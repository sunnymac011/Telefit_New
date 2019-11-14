package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.RecipeListAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityNewRecipeBinding;
import fit.tele.com.telefit.helper.OnStartDragListener;
import fit.tele.com.telefit.helper.SimpleItemTouchHelperCallback;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.NewRecipeBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateRecipeActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener {

    ActivityNewRecipeBinding binding;
    private ArrayList<ChompProductBean> chompProductBeans;
    private RecipeListAdapter recipeListAdapter1;
    private NewRecipeBean newRecipeBean;
    private ItemTouchHelper mItemTouchHelper;

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

//        if (!TextUtils.isEmpty(preferences.getRecipeNameDataPref()))
//            binding.inputRecipeName.setText(preferences.getRecipeNameDataPref());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvRecipe.setLayoutManager(linearLayoutManager);
        if (recipeListAdapter1 == null) {
            recipeListAdapter1 = new RecipeListAdapter(context, this, new RecipeListAdapter.ClickListener() {
                @Override
                public void onClick(ChompProductBean chompProductBean) {

                }
            });
        }
        binding.rvRecipe.setAdapter(recipeListAdapter1);
        recipeListAdapter1.clearAll();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recipeListAdapter1);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rvRecipe);
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
//                if (TextUtils.isEmpty(binding.inputRecipeName.getText()))
//                    preferences.saveRecipeNameData(binding.inputRecipeName.getText().toString().trim());
                intent = new Intent(context, SearchFoodActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                preferences.cleanRecipedata();
                if (TextUtils.isEmpty(binding.inputRecipeName.getText()))
                    CommonUtils.toast(context, "Please give Recipe Name");
                else {
                    if (chompProductBeans.size() > 0)
                    {
                        newRecipeBean = new NewRecipeBean();
                        newRecipeBean.setRecipeName(binding.inputRecipeName.getText().toString().trim());
                        newRecipeBean.setIsRacipeMeal("1");
                        newRecipeBean.setFood(chompProductBeans);
                        callCreateApi();
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
                            Log.e("callCreateApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<NewRecipeBean> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getStatus() == 1) {
                                preferences.cleanRoutinedata();
                                preferences.cleanRoutineHeaderedata();
                                Intent intent = new Intent(context, SearchFoodActivity.class);
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
}
