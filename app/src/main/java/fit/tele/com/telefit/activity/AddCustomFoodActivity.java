package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.AddCustomFoodAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityAddCustomFoodBinding;
import fit.tele.com.telefit.dialog.AddNutritionDialog;
import fit.tele.com.telefit.dialog.UpdateNutritionDialog;
import fit.tele.com.telefit.modelBean.CustomFoodBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.NewRecipeBean;
import fit.tele.com.telefit.modelBean.NutritionLabelBean;
import fit.tele.com.telefit.modelBean.chompBeans.Calcium;
import fit.tele.com.telefit.modelBean.chompBeans.Calories;
import fit.tele.com.telefit.modelBean.chompBeans.Carbohydrates;
import fit.tele.com.telefit.modelBean.chompBeans.Cholesterol;
import fit.tele.com.telefit.modelBean.chompBeans.Fat;
import fit.tele.com.telefit.modelBean.chompBeans.Fiber;
import fit.tele.com.telefit.modelBean.chompBeans.Iron;
import fit.tele.com.telefit.modelBean.chompBeans.NovaGroup;
import fit.tele.com.telefit.modelBean.chompBeans.NutritionLabel;
import fit.tele.com.telefit.modelBean.chompBeans.Proteins;
import fit.tele.com.telefit.modelBean.chompBeans.Salt;
import fit.tele.com.telefit.modelBean.chompBeans.SaturatedFat;
import fit.tele.com.telefit.modelBean.chompBeans.Sodium;
import fit.tele.com.telefit.modelBean.chompBeans.Sugars;
import fit.tele.com.telefit.modelBean.chompBeans.TransFat;
import fit.tele.com.telefit.modelBean.chompBeans.VitaminA;
import fit.tele.com.telefit.modelBean.chompBeans.VitaminC;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCustomFoodActivity extends BaseActivity {

    private ActivityAddCustomFoodBinding binding;
    private AddCustomFoodAdapter addCustomFoodAdapter;
    private NutritionLabelBean nutritionLabelBean;
    private ArrayList<NutritionLabelBean> nutritionLabelBeans = new ArrayList<>();
    private AddNutritionDialog addNutritionDialog;
    private ArrayList<String> nutritionList = new ArrayList<>();
    private CustomFoodBean customFoodBean;
    private UpdateNutritionDialog updateNutritionDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_custom_food;
    }

    @Override
    public void init() {
        binding = (ActivityAddCustomFoodBinding) getBindingObj();
        nutritionLabelBean = new NutritionLabelBean();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvNutrDetails.setLayoutManager(linearLayoutManager);

        setListner();
    }

    private void setListner() {
        if (addCustomFoodAdapter == null) {
            addCustomFoodAdapter = new AddCustomFoodAdapter(context, new AddCustomFoodAdapter.ClickListener() {
                @Override
                public void onEditClick(NutritionLabelBean nutritionLabelBean) {
                    updateNutritionDialog = new UpdateNutritionDialog(context, nutritionLabelBean, new UpdateNutritionDialog.SetDataListener() {
                        @Override
                        public void onContinueClick(String name, String measur, String per100, String serving) {
                            if (nutritionLabelBeans != null) {
                                for (int i=0;i<nutritionLabelBeans.size();i++) {
                                    if (nutritionLabelBeans.get(i).getNutritionType().equalsIgnoreCase(name)) {
                                        nutritionLabelBeans.get(i).setNutritionType(name);
                                        nutritionLabelBeans.get(i).setMeasurement(measur);
                                        nutritionLabelBeans.get(i).setPer100g(per100);
                                        nutritionLabelBeans.get(i).setPerServing(serving);
                                        addCustomFoodAdapter.clearAll();
                                        addCustomFoodAdapter.addAllList(nutritionLabelBeans);
                                    }
                                }
                            }
                        }
                    });
                    updateNutritionDialog.show();
                }

                @Override
                public void onDeleteClick(int pos) {
                    if (nutritionLabelBeans != null && nutritionLabelBeans.size() >= pos)
                        nutritionLabelBeans.remove(pos);
                }
            });
        }
        binding.rvNutrDetails.setAdapter(addCustomFoodAdapter);
        addCustomFoodAdapter.clearAll();

//        nutritionLabelBeans.add(nutritionLabelBean);
//        addCustomFoodAdapter.addAllList(nutritionLabelBeans);

        binding.txtAddNutr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nutritionList != null && nutritionList.size()>0) {
                    addNutritionDialog = new AddNutritionDialog(context, nutritionList, new AddNutritionDialog.SetDataListener() {
                        @Override
                        public void onContinueClick(String name, String measur, String per100, String serving) {
                            if (nutritionLabelBeans != null) {
                                nutritionLabelBean = new NutritionLabelBean();
                                nutritionLabelBean.setNutritionType(name);
                                nutritionLabelBean.setMeasurement(measur);
                                nutritionLabelBean.setPer100g(per100);
                                nutritionLabelBean.setPerServing(serving);
                                nutritionLabelBeans.add(nutritionLabelBean);
                                addCustomFoodAdapter.clearAll();
                                addCustomFoodAdapter.addAllList(nutritionLabelBeans);
                            }
                        }
                    });
                    addNutritionDialog.show();
                }
                else
                    CommonUtils.toast(context,"Please try again later!");
            }
        });

        binding.txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nutritionLabelBeans != null && nutritionLabelBeans.size() > 0) {
                    customFoodBean = new CustomFoodBean();
                    customFoodBean.setName(binding.inputName.getText().toString());
                    customFoodBean.setManufacturer(binding.inputManuf.getText().toString());
                    customFoodBean.setBarcode(binding.inputBar.getText().toString());
                    customFoodBean.setIngredients(binding.inputIngr.getText().toString());
                    customFoodBean.setPackageSize(binding.inputPs.getText().toString());
                    customFoodBean.setServingSize(binding.inputSs.getText().toString());
                    setNutrition(nutritionLabelBeans);
                    calladdCustomFoodApi(customFoodBean);
                }
                else
                    CommonUtils.toast(context,"Please add Nutritions!");
            }
        });

        callNutritionApi();
    }

    private void callNutritionApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<String>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getNutritionApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<String>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callNutritionApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<String>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                nutritionList = exercisesBean.getResult();
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void calladdCustomFoodApi(CustomFoodBean customFoodBean) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<CustomFoodBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).addCustomFood(customFoodBean);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<CustomFoodBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("calladdCustomFoodApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<CustomFoodBean> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getStatus() == 1) {
                                Intent intent = new Intent(context, SearchFoodActivity.class);
                                intent.putExtra("tab",2);
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

    private void setNutrition(ArrayList<NutritionLabelBean> nutritionLabelBeans) {

        NutritionLabel nutritionLabel = new NutritionLabel();
        for (int i=0;i<nutritionLabelBeans.size();i++)
        {
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("calcium")) {
                Calcium calcium = new Calcium();
                calcium.setName(nutritionLabelBeans.get(i).getNutritionType());
                calcium.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                calcium.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                calcium.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setCalcium(calcium);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("carbohydrates")) {
                Carbohydrates carbohydrates = new Carbohydrates();
                carbohydrates.setName(nutritionLabelBeans.get(i).getNutritionType());
                carbohydrates.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                carbohydrates.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                carbohydrates.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setCarbohydrates(carbohydrates);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("cholesterol")) {
                Cholesterol cholesterol = new Cholesterol();
                cholesterol.setName(nutritionLabelBeans.get(i).getNutritionType());
                cholesterol.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                cholesterol.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                cholesterol.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setCholesterol(cholesterol);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("calories")) {
                Calories calories = new Calories();
                calories.setName(nutritionLabelBeans.get(i).getNutritionType());
                calories.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                calories.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                calories.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setCalories(calories);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("fat")) {
                Fat fat = new Fat();
                fat.setName(nutritionLabelBeans.get(i).getNutritionType());
                fat.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                fat.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                fat.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setFat(fat);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("fiber")) {
                Fiber fiber = new Fiber();
                fiber.setName(nutritionLabelBeans.get(i).getNutritionType());
                fiber.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                fiber.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                fiber.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setFiber(fiber);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("iron")) {
                Iron iron = new Iron();
                iron.setName(nutritionLabelBeans.get(i).getNutritionType());
                iron.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                iron.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                iron.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setIron(iron);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("nova-group")) {
                NovaGroup novaGroup = new NovaGroup();
                novaGroup.setName(nutritionLabelBeans.get(i).getNutritionType());
                novaGroup.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                novaGroup.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                novaGroup.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setNovaGroup(novaGroup);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("proteins")) {
                Proteins proteins = new Proteins();
                proteins.setName(nutritionLabelBeans.get(i).getNutritionType());
                proteins.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                proteins.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                proteins.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setProteins(proteins);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("salt")) {
                Salt salt = new Salt();
                salt.setName(nutritionLabelBeans.get(i).getNutritionType());
                salt.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                salt.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                salt.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setSalt(salt);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("saturated-fat")) {
                SaturatedFat saturatedFat = new SaturatedFat();
                saturatedFat.setName(nutritionLabelBeans.get(i).getNutritionType());
                saturatedFat.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                saturatedFat.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                saturatedFat.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setSaturatedFat(saturatedFat);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("sodium")) {
                Sodium sodium = new Sodium();
                sodium.setName(nutritionLabelBeans.get(i).getNutritionType());
                sodium.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                sodium.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                sodium.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setSodium(sodium);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("sugars")) {
                Sugars sugars = new Sugars();
                sugars.setName(nutritionLabelBeans.get(i).getNutritionType());
                sugars.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                sugars.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                sugars.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setSugars(sugars);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("trans-fat")) {
                TransFat transFat = new TransFat();
                transFat.setName(nutritionLabelBeans.get(i).getNutritionType());
                transFat.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                transFat.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                transFat.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setTransFat(transFat);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("vitamin-a")) {
                VitaminA vitaminA = new VitaminA();
                vitaminA.setName(nutritionLabelBeans.get(i).getNutritionType());
                vitaminA.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                vitaminA.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                vitaminA.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setVitaminA(vitaminA);
            }
            if (nutritionLabelBeans.get(i).getNutritionType().toString().equalsIgnoreCase("vitamin-c")) {
                VitaminC vitaminC = new VitaminC();
                vitaminC.setName(nutritionLabelBeans.get(i).getNutritionType());
                vitaminC.setMeasurement(nutritionLabelBeans.get(i).getMeasurement());
                vitaminC.setPer100g(nutritionLabelBeans.get(i).getPer100g());
                vitaminC.setPerServing(nutritionLabelBeans.get(i).getPerServing());
                nutritionLabel.setVitaminC(vitaminC);
            }
        }
        if (nutritionLabel != null)
            customFoodBean.setNutritionLabel(nutritionLabel);
    }
}
