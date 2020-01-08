package fit.tele.com.telefit.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.FoodAdapter;
import fit.tele.com.telefit.adapter.FoodCategoryAdapter;
import fit.tele.com.telefit.adapter.MealAdapter;
import fit.tele.com.telefit.adapter.NewRecipeAdapter;
import fit.tele.com.telefit.adapter.RecipeFoodAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySearchFoodBinding;
import fit.tele.com.telefit.modelBean.FoodCategoryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.NewRecipeBean;
import fit.tele.com.telefit.modelBean.RecipeListBean;
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
    private RecipeFoodAdapter recipeFoodAdapter;
    private NewRecipeAdapter newRecipeAdapter;
    private RecyclerView FoodRv,rv_myfood,rv_meals, rv_recipe;
    private LinearLayoutManager linearLayoutManager;
    private EditText foodSv, edt_recipes, edt_meals,edt_my_food;
    private ArrayList<ChompProductBean> chompProductBeans;
    private int strSelectedTab = 1, strTab = 0;
    private MealAdapter mealAdapter;
    private ImageView img_search_barcode,img_my_barcode,img_meals_barcode,img_recipes_barcode;
    private LinearLayout ll_add_meal,ll_add_recipe;
    private ImageView img_search;

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
        binding.llSocial.setOnClickListener(this);

        binding.llSearchTab.setOnClickListener(this);
        binding.llFoodTab.setOnClickListener(this);
        binding.llMealsTab.setOnClickListener(this);
        binding.llRecipesTab.setOnClickListener(this);
        binding.txtNew.setOnClickListener(this);

        FoodRv = (RecyclerView) findViewById(R.id.rv_food);
        foodSv = (EditText) findViewById(R.id.edt_search_food);
        edt_recipes = (EditText) findViewById(R.id.edt_recipes);
        edt_meals = (EditText) findViewById(R.id.edt_meals);
        edt_my_food = (EditText) findViewById(R.id.edt_my_food);
        img_search_barcode = (ImageView) findViewById(R.id.img_search_barcode);
        img_my_barcode = (ImageView) findViewById(R.id.img_my_barcode);
        img_meals_barcode = (ImageView) findViewById(R.id.img_meals_barcode);
        img_recipes_barcode = (ImageView) findViewById(R.id.img_recipes_barcode);
        img_search = (ImageView) findViewById(R.id.img_search);
        ll_add_meal = (LinearLayout) findViewById(R.id.ll_add_meal);
        ll_add_recipe = (LinearLayout) findViewById(R.id.ll_add_recipe);

        img_search_barcode.setOnClickListener(this);
        img_my_barcode.setOnClickListener(this);
        img_meals_barcode.setOnClickListener(this);
        img_recipes_barcode.setOnClickListener(this);

        if(getIntent() != null && getIntent().hasExtra("tab"))
            strTab = getIntent().getIntExtra("tab", 0);

        if (strTab == 2)
            binding.llFoodTab.performClick();
        else if (strTab == 3)
            binding.llMealsTab.performClick();
        else if (strTab == 4)
            binding.llRecipesTab.performClick();
        else
            setSearchFoodData();

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
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
            case R.id.img_search_barcode:
                intent = new Intent(context, QRCodeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.img_my_barcode:
                intent = new Intent(context, QRCodeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.img_meals_barcode:
                intent = new Intent(context, QRCodeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.img_recipes_barcode:
                intent = new Intent(context, QRCodeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
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
                setMyFoodData();
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
                setMealData();
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
                preferences.cleanMealNamedata();
                preferences.cleanMealDatedata();
                preferences.cleanMealdata();
                preferences.cleanMealIDdata();
                preferences.cleanUpdateMeal();
                preferences.cleanRecipeNamedata();
                preferences.cleanRecipedata();

                if (strSelectedTab == 3)
                    intent = new Intent(context, NewMealActivity.class);
                else if (strSelectedTab == 4)
                    intent = new Intent(context, NewRecipeActivity.class);
                else
                    intent = new Intent(context, AddCustomFoodActivity.class);

                startActivity(intent);
                overridePendingTransition(0, 0);

                break;
        }
    }

    private void setSearchFoodData() {
        binding.txtNew.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        FoodRv.setLayoutManager(linearLayoutManager);

//        if (foodAdapter == null) {
            foodAdapter = new FoodAdapter(context, preferences, FoodRv);
            foodAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                }
            });
//        }
        FoodRv.setAdapter(foodAdapter);
        foodAdapter.clearAll();

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(foodSv.getText().toString().trim()))
                    callChompApi(foodSv.getText().toString().trim());

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

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

        callMyFoodCatApi("2");
    }

    private void setMyFoodData() {
        binding.txtNew.setVisibility(View.VISIBLE);
        rv_myfood = (RecyclerView) findViewById(R.id.rv_myfood);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_myfood.setLayoutManager(linearLayoutManager);
        if (recipeFoodAdapter == null) {
            recipeFoodAdapter = new RecipeFoodAdapter(context, preferences, rv_myfood, new RecipeFoodAdapter.ClickListner() {
                @Override
                public void onDeleteClick(String id) {
                    callDeleteFoodApi(id);
                }
            });
        }
        rv_myfood.setAdapter(recipeFoodAdapter);
        recipeFoodAdapter.clearAll();

        edt_my_food.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recipeFoodAdapter.filter(edt_my_food.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        callCustomFoodApi();
    }

    private void setRecipeData() {
        binding.txtNew.setVisibility(View.VISIBLE);
        rv_recipe = (RecyclerView) findViewById(R.id.rv_recipe);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_recipe.setLayoutManager(linearLayoutManager);
        if (newRecipeAdapter == null) {
            newRecipeAdapter = new NewRecipeAdapter(context, preferences, rv_recipe, new NewRecipeAdapter.ClickListner() {
                @Override
                public void onDeleteClick(String id) {
                    callDeleteRecipeApi(id);
                }
            });
        }
        rv_recipe.setAdapter(newRecipeAdapter);
        newRecipeAdapter.clearAll();

        edt_my_food.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newRecipeAdapter.filter(edt_my_food.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        callGetRecipeApi();
    }

    private void setMealData() {
        binding.txtNew.setVisibility(View.GONE);
        rv_meals = (RecyclerView) findViewById(R.id.rv_meals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_meals.setLayoutManager(linearLayoutManager);
        mealAdapter = null;
        mealAdapter = new MealAdapter(context, new MealAdapter.ClickListener() {
            @Override
            public void onClick(FoodCategoryBean categoryBean, boolean isBarcode) {
                if (isBarcode) {
                    Intent intent = new Intent(SearchFoodActivity.this, QRCodeActivity.class);
                    startActivity(intent);
                }
                else {
                    preferences.cleanRecipedata();
                    preferences.cleanMealNamedata();
                    preferences.cleanMealdata();
                    preferences.saveMealIDData(categoryBean.getId());
                    preferences.cleanRecipedata();
                    preferences.cleanRecipeNamedata();
                    Intent intent = new Intent(SearchFoodActivity.this, NewMealActivity.class);
                    intent.putExtra("recipeName",categoryBean.getFoodType());
                    intent.putExtra("foodCatID",categoryBean.getId());
                    if (categoryBean.getRecipeBeans().get(0) != null)
                        intent.putExtra("recipeID",categoryBean.getRecipeBeans().get(0).getId());
                    else
                        intent.putExtra("recipeID","0");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onDeletClick(String foodCatId, FoodCategoryBean categoryBean) {

            }

            @Override
            public void onAddClick(FoodCategoryBean categoryBean) {
                callGetMealApi(categoryBean.getId(),categoryBean.getRecipeBeans().get(0).getId());
            }
        });
        rv_meals.setAdapter(mealAdapter);
        mealAdapter.clearAll();

        edt_meals.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mealAdapter.filter(edt_meals.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ll_add_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newSnack = (mealAdapter.getItemCount()-5)+3;
                Intent intent = new Intent(SearchFoodActivity.this, NewMealActivity.class);
                intent.putExtra("recipeName","Snack"+newSnack);
                intent.putExtra("foodCatID","0");
                intent.putExtra("recipeID","0");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        callFoodCatApi("2");
    }

    private void callGetRecipeApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", "1");

            Observable<ModelBean<ArrayList<RecipeListBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getRecipeApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<RecipeListBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetRecipeApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<RecipeListBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getResult() != null && apiFoodBean.getResult().size() > 0) {
                                newRecipeAdapter.addAllList(apiFoodBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callChompApi(String strName) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Observable<ResponseBody> signupusers;

            if (preferences.getCountyPref() != null && !TextUtils.isEmpty(preferences.getCountyPref()))
                signupusers = FetchServiceBase.getChompFetcherService(context).getChompCountry("2smoc98kRRQNtsihq8",strName, preferences.getCountyPref());
            else
                signupusers = FetchServiceBase.getChompFetcherService(context).getChomp("2smoc98kRRQNtsihq8",strName);
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
                                Log.e("Exception ",""+e.getMessage()+"\n msg "+e.getCause());
                                CommonUtils.toast(context,"No Data Found!");
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callFoodCatApi(String isRecipe) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", isRecipe);

            Observable<ModelBean<ArrayList<FoodCategoryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getFoodPlansApi(map);
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
                                mealAdapter.addAllList(apiFoodBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, ""+apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callMyFoodCatApi(String isRecipe) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", isRecipe);

            Observable<ModelBean<ArrayList<ChompProductBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getMyFoodApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ChompProductBean>>>() {
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
                        public void onNext(ModelBean<ArrayList<ChompProductBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getStatus().toString().equalsIgnoreCase("1") && apiFoodBean.getResult() != null)
                            {
                                foodAdapter.addAllList(apiFoodBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, ""+apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callCustomFoodApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("food_count", preferences.getMyFoodListPref().trim());

            Observable<ModelBean<ArrayList<ChompProductBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCustomFoodApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ChompProductBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCustomFoodApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ChompProductBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getResult() != null && apiFoodBean.getResult().size() > 0) {
                                recipeFoodAdapter.addAllList(apiFoodBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callDeleteFoodApi(String foodId) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("food_id", foodId);

            Observable<ModelBean<ArrayList<ChompProductBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).deleteFood(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ChompProductBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callDeleteFoodApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ChompProductBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            recipeFoodAdapter.clearAll();
                            if (apiFoodBean.getResult() != null && apiFoodBean.getResult().size() > 0)
                                recipeFoodAdapter.addAllList(apiFoodBean.getResult());
                            else
                                CommonUtils.toast(context, apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callDeleteRecipeApi(String foodId) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("recipe_id", foodId);
            map.put("is_racipe_meal", "1");

            Observable<ModelBean<ArrayList<RecipeListBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).deleteRecipe(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<RecipeListBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callDeleteFoodApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<RecipeListBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            newRecipeAdapter.clearAll();
                            if (apiFoodBean.getStatus() == 1)
                            {
                                if (apiFoodBean.getResult() != null && apiFoodBean.getResult().size() > 0)
                                    newRecipeAdapter.addAllList(apiFoodBean.getResult());
                                else
                                    CommonUtils.toast(context, apiFoodBean.getMessage());
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
                                preferences.cleanMealdata();
                                preferences.saveMealData(chompProductBeans);
                                Intent intent = new Intent(context, NewMealActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
