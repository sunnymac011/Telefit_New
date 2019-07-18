package fit.tele.com.telefit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExercisesDragNDropAdapter;
import fit.tele.com.telefit.adapter.FoodAdapter;
import fit.tele.com.telefit.adapter.RecipeListAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySearchFoodBinding;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.OnLoadMoreListener;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchFoodActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySearchFoodBinding binding;
    private FoodAdapter foodAdapter;
    private RecyclerView FoodRv;
    private LinearLayoutManager linearLayoutManager;
    private EditText foodSv;
    private ArrayList<ChompProductBean> chompProductBeans;
    private int strSelectedTab = 1;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search_food;
    }

    @Override
    public void init() {
        binding = (ActivitySearchFoodBinding) getBindingObj();
        setListner();
    }

    private void setListner() {
        chompProductBeans = new ArrayList<>();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);

        binding.llSearchTab.setOnClickListener(this);
        binding.llFoodTab.setOnClickListener(this);
        binding.llMealsTab.setOnClickListener(this);
        binding.llRecipesTab.setOnClickListener(this);
        binding.txtNew.setOnClickListener(this);

        FoodRv = (RecyclerView) findViewById(R.id.rv_food);
        foodSv = (EditText) findViewById(R.id.edt_search_food);

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        setSearchFoodData();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
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

            case R.id.ll_search_tab:
                strSelectedTab = 1;
                binding.vf.setDisplayedChild(0);
                binding.txtSearchTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewSearch.setVisibility(View.VISIBLE);
                binding.txtFoodTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFood.setVisibility(View.GONE);
                binding.txtMealsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMeals.setVisibility(View.GONE);
                binding.txtRecipesTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewRecipes.setVisibility(View.GONE);
                setSearchFoodData();
                break;
            case R.id.ll_food_tab:
                strSelectedTab = 2;
                binding.vf.setDisplayedChild(1);
                binding.txtSearchTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewSearch.setVisibility(View.GONE);
                binding.txtFoodTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewFood.setVisibility(View.VISIBLE);
                binding.txtMealsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMeals.setVisibility(View.GONE);
                binding.txtRecipesTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewRecipes.setVisibility(View.GONE);
                break;
            case R.id.ll_meals_tab:
                strSelectedTab = 3;
                binding.vf.setDisplayedChild(2);
                binding.txtSearchTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewSearch.setVisibility(View.GONE);
                binding.txtFoodTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFood.setVisibility(View.GONE);
                binding.txtMealsTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewMeals.setVisibility(View.VISIBLE);
                binding.txtRecipesTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewRecipes.setVisibility(View.GONE);
                break;
            case R.id.ll_recipes_tab:
                strSelectedTab = 4;
                binding.vf.setDisplayedChild(3);
                binding.txtSearchTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewSearch.setVisibility(View.GONE);
                binding.txtFoodTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFood.setVisibility(View.GONE);
                binding.txtMealsTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMeals.setVisibility(View.GONE);
                binding.txtRecipesTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewRecipes.setVisibility(View.VISIBLE);
                setRecipeData();
                break;

            case R.id.txt_new:
                if (strSelectedTab == 3) {
                    intent = new Intent(context, NewRecipeActivity.class);
                }
                else {
                    intent = new Intent(context, NewRecipeActivity.class);
                }
                startActivity(intent);
                this.overridePendingTransition(0, 0);

                break;
        }
    }

    private void setSearchFoodData() {
        FoodRv.setLayoutManager(linearLayoutManager);

        if (foodAdapter == null) {
            foodAdapter = new FoodAdapter(context, FoodRv);
            foodAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                }
            });
        }
        FoodRv.setAdapter(foodAdapter);
        foodAdapter.clearAll();

        foodSv.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(foodSv.getText().toString().trim()))
                        callChompApi(foodSv.getText().toString().trim());

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    return true;
                }
                return false;
            }
        });
    }

    private void setRecipeData() {

    }

    private void callChompApi(String strName) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ResponseBody> signupusers = FetchServiceBase.getChompFetcherService(context).getChomp("2smoc98kRRQNtsihq8",strName);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callChompApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ResponseBody chompJSON) {
                            binding.progress.setVisibility(View.GONE);
                            chompProductBeans.clear();
                            foodAdapter.clearAll();
                            try {
                                JSONObject jsonObject = new JSONObject(chompJSON.string());
                                JSONObject productObject = jsonObject.getJSONObject("products");
                                Iterator<String> iter = productObject.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    String value = productObject.getString(key);
                                    chompProductBeans.add(new Gson().fromJson(value.toString(), ChompProductBean.class));
                                }
                                foodAdapter.addAllList(chompProductBeans);
                            } catch (Exception e) {
                                e.printStackTrace();
                                CommonUtils.toast(context,"No Data Found!");
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
