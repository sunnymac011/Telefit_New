package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityAddFoodBinding;
import fit.tele.com.telefit.dialog.AddFoodDialog;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RecipeListBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.modelBean.chompBeans.Details;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddFoodActivity extends BaseActivity implements View.OnClickListener {

    ActivityAddFoodBinding binding;
    private ChompProductBean chompProductBean;
    private int intQty = 1;
    private double doubleFiber = 0, doubleSugar = 0, doubleCarbs = 0, doubleCalories = 0, doubleFat = 0, doubleSatFat = 0, doubleChol = 0
            , doubleSodium = 0, doubleProtein = 0,doubleHalfQty = 0;
    private ArrayList<ChompProductBean> chompProductBeans = new ArrayList<>();
    private AddFoodDialog addFoodDialog;
    private String strFrom = "",recipeId = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_food;
    }

    @Override
    public void init() {
        binding = (ActivityAddFoodBinding) getBindingObj();
        if(getIntent() != null && getIntent().hasExtra("from"))
            strFrom = getIntent().getStringExtra("from");
        if (!TextUtils.isEmpty(strFrom) && strFrom.equalsIgnoreCase("RecipeAdapter")) {
            if(getIntent() != null && getIntent().hasExtra("SelectedRecipe"))
                recipeId = getIntent().getStringExtra("SelectedRecipe");
            callGetRecipeDetailsApi(recipeId);
        }
        else {

            if(getIntent() != null && getIntent().hasExtra("SelectedItems"))
                chompProductBean = getIntent().getParcelableExtra("SelectedItems");
            if (chompProductBean != null)
                setValue();
            setListner();
        }
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
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        binding.txtAdd.setOnClickListener(this);

        final String[] values= {"-","1/4","1/2", "3/4"};

        binding.npServeQty.setMinValue(0);
        binding.npServeQty.setMaxValue(values.length-1);
        binding.npServeQty.setDisplayedValues(values);
        binding.npServeQty.setWrapSelectorWheel(true);
        binding.npServeQty.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                if (newVal == 0)
                    doubleHalfQty = 0;
                if (newVal == 1)
                    doubleHalfQty = 0.25;
                if (newVal == 2)
                    doubleHalfQty = 0.5;
                if (newVal == 3)
                    doubleHalfQty = 0.75;

                setData();
            }
        });

        if (chompProductBean.getServingQtySecond() == 0.25)
            binding.npServeQty.setValue(1);
        if (chompProductBean.getServingQtySecond() == 0.5)
            binding.npServeQty.setValue(2);
        if (chompProductBean.getServingQtySecond() == 0.75)
            binding.npServeQty.setValue(3);

        final String[] newValues= {"-","1","2", "3", "4", "5", "6", "7", "8", "9", "10"};
        binding.npQty.setMinValue(0);
        binding.npQty.setMaxValue(newValues.length-1);
        binding.npQty.setDisplayedValues(newValues);
        binding.npQty.setWrapSelectorWheel(true);
        binding.npQty.setValue(1);
        binding.npQty.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                if (newVal == 0)
                    intQty = 0;
                else
                    intQty = Integer.parseInt(newValues[newVal]);

                Log.e("intQty",""+intQty);

                setData();
            }
        });

        if (chompProductBean.getServingQty() != 0)
            binding.npQty.setValue(chompProductBean.getServingQty());
    }

    private void setValue() {
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getServingSize() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getServingSize()))
            binding.txtServingSize.setText(chompProductBean.getDetails().getServingSize());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getCalories() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getCalories().getPerServing()))
            doubleCalories = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getCalories().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getFat() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getFat().getPerServing()))
            doubleFat = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getFat().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getCholesterol() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getCholesterol().getPerServing()))
            doubleChol = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getCholesterol().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getSodium() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getSodium().getPerServing()))
            doubleSodium = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getSodium().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getFiber() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getFiber().getPerServing()))
            doubleFiber = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getFiber().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getSugars() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getSugars().getPerServing()))
            doubleSugar = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getSugars().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getCarbohydrates() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getCarbohydrates().getPerServing()))
            doubleCarbs = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getCarbohydrates().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getProteins() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getProteins().getPerServing()))
            doubleProtein = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getProteins().getPerServing());
        if (chompProductBean.getDetails() != null
                && chompProductBean.getDetails().getNutritionLabel() != null
                && chompProductBean.getDetails().getNutritionLabel().getSaturatedFat() != null
                && !TextUtils.isEmpty(chompProductBean.getDetails().getNutritionLabel().getSaturatedFat().getPerServing()))
            doubleSatFat = Double.parseDouble(chompProductBean.getDetails().getNutritionLabel().getSaturatedFat().getPerServing());

        if(chompProductBean.getDetails() != null && chompProductBean.getDetails().getImages() != null &&
                chompProductBean.getDetails().getImages().getFront() != null &&
                chompProductBean.getDetails().getImages().getFront().getSmall() != null && !TextUtils.isEmpty(chompProductBean.getDetails().getImages().getFront().getSmall())) {
            Picasso.with(context)
                    .load(chompProductBean.getDetails().getImages().getFront().getSmall())
                    .error(R.drawable.empty_food)
                    .placeholder(R.drawable.empty_food)
                    .transform(new CircleTransform())
                    .into(binding.imgFood);
        }
        else
            binding.imgFood.setImageResource(R.drawable.empty_food);

        if (chompProductBean.getServingQtySecond() != 0)
            doubleHalfQty = chompProductBean.getServingQtySecond();
        if (chompProductBean.getServingQty() != 0)
            intQty = chompProductBean.getServingQty();
        setData();
    }

    private void setData() {
        if (chompProductBean.getName() != null && !TextUtils.isEmpty(chompProductBean.getName()))
            binding.txtFoodName.setText(Html.fromHtml(chompProductBean.getName()));

        double convertedCal = doubleCalories/4.184;

        if (doubleHalfQty != 0) {
            binding.txtCal.setText(String.format("%.2f", (convertedCal*intQty+(convertedCal*doubleHalfQty)))+"");
            binding.txtTotalFat.setText(String.format("%.2f", (doubleFat*intQty+(doubleFat*doubleHalfQty)))+"g");
            binding.txtCholesterol.setText(String.format("%.2f", (doubleChol*intQty+(doubleChol*doubleHalfQty)))+"mg");
            binding.txtSodium.setText(String.format("%.2f", (doubleSodium*intQty+(doubleSodium*doubleHalfQty)))+"g");
            binding.txtFibre.setText(String.format("%.2f", (doubleFiber*intQty+(doubleFiber*doubleHalfQty)))+"g");
            binding.txtSugars.setText(String.format("%.2f", (doubleSugar*intQty+(doubleSugar*doubleHalfQty)))+"g");
            binding.txtTotalCarbs.setText(String.format("%.2f", (doubleCarbs*intQty+(doubleCarbs*doubleHalfQty)))+"g");
            binding.txtProtein.setText(String.format("%.2f", (doubleProtein*intQty+(doubleProtein*doubleHalfQty)))+"g");
            binding.txtSatFat.setText(String.format("%.2f", (doubleSatFat*intQty+(doubleSatFat*doubleHalfQty)))+"g");
        }
        else {
            binding.txtCal.setText(String.format("%.2f", (convertedCal*intQty))+"");
            binding.txtTotalFat.setText(String.format("%.2f", (doubleFat*intQty))+"g");
            binding.txtCholesterol.setText(String.format("%.2f", (doubleChol*intQty))+"mg");
            binding.txtSodium.setText(String.format("%.2f", (doubleSodium*intQty))+"g");
            binding.txtFibre.setText(String.format("%.2f", (doubleFiber*intQty))+"g");
            binding.txtSugars.setText(String.format("%.2f", (doubleSugar*intQty))+"g");
            binding.txtTotalCarbs.setText(""+String.format("%.2f", (doubleCarbs*intQty))+"g");
            binding.txtProtein.setText(String.format("%.2f", (doubleProtein*intQty))+"g");
            binding.txtSatFat.setText(String.format("%.2f", (doubleSatFat*intQty))+"g");
        }

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

            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_add:
                if (preferences.getRecipeDataPref() != null) {
                    Gson gson = new Gson();
                    chompProductBeans = gson.fromJson(preferences.getRecipeDataPref(), new TypeToken<List<ChompProductBean>>(){}.getType());
                }
                else if (preferences.getMealDataPref() != null) {
                    Gson gson = new Gson();
                    chompProductBeans = gson.fromJson(preferences.getMealDataPref(), new TypeToken<List<ChompProductBean>>(){}.getType());
                }
                else
                    chompProductBeans = new ArrayList<>();

                chompProductBean.setServingQty(intQty);
                chompProductBean.setServingQtySecond(doubleHalfQty);
                if (doubleHalfQty != 0)
                    chompProductBean.setTotalCalories(String.format("%.2f", (doubleCalories*intQty+(doubleCalories*doubleHalfQty))));
                else
                    chompProductBean.setTotalCalories(String.format("%.2f", (doubleCalories*intQty)));

                if (strFrom.equalsIgnoreCase("NewRecipeActivity") || strFrom.equalsIgnoreCase("NewMealActivity")) {
                    for (int i=0;i<chompProductBeans.size();i++)
                    {
                        if (chompProductBean.getProductId().equalsIgnoreCase(chompProductBeans.get(i).getProductId()))
                        {
                            chompProductBeans.set(i, chompProductBean);
                        }
                    }
                }
                else
                    chompProductBeans.add(chompProductBean);

                if (preferences.getRecipeNameDataPref() != null) {
                    preferences.saveRecipeData(chompProductBeans);
                    intent = new Intent(context, NewRecipeActivity.class);
                }
                else {
                    preferences.saveMealData(chompProductBeans);
                    intent = new Intent(context, NewMealActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    private void callGetRecipeDetailsApi(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_racipe_meal", "1");
            map.put("recipe_id", id);

            Observable<ModelBean<ChompProductBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getRecipeDetailsApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ChompProductBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetRecipeDetailsApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ChompProductBean> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getResult() != null)
                            {
                                chompProductBean = apiFoodBean.getResult();
                                chompProductBean.setManufacturer("");
                                chompProductBean.setUpc("");
                                if (chompProductBean != null)
                                    setValue();
                                setListner();
                            }
                            else
                                CommonUtils.toast(context, apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
