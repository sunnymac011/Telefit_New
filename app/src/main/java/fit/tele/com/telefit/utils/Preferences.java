package fit.tele.com.telefit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.RoutinePrefBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;


public class Preferences {

    private Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public void setPushToken(String token) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pushtoken", token);
        editor.apply();
    }

    public String getPushToken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("pushtoken", "");
    }

    public void setSessionToken(String uid) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionToken", uid);
        editor.apply();
    }

    public String getSessionToken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("sessionToken", null);
    }

    public void saveUserData(LoginBean userData) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(userData);
        editor.putString("user_data_obj", json);
        editor.apply();
        editor.commit();
    }

    public LoginBean getUserDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("user_data_obj", null);
        return gson.fromJson(json, LoginBean.class);
    }

    public void setTheme(String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("themeName", name);
        editor.apply();
    }

    public String getTheme() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("themeName", "Default");
    }

    public void cleanAlltoken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionToken", null);
        editor.apply();
    }

    public void setWeightMes(String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("weight_mes", name);
        editor.apply();
    }

    public String getWeightMes() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("weight_mes", "Pound");
    }

    public void setMesurementsMes(String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mesurements", name);
        editor.apply();
    }

    public String getMesurementsMes() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("mesurements", "Feet and Inches");
    }

    public void setDistanceMes(String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("distance_mes", name);
        editor.apply();
    }

    public String getDistanceMes() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("distance_mes", "Miles");
    }

    public void setEnergyMes(String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("energy_mes", name);
        editor.apply();
    }

    public String getEnergyMes() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("energy_mes", "Calories");
    }

    public void setLiquidMes(String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("liquid_mes", name);
        editor.apply();
    }

    public String getLiquidMes() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("liquid_mes", "Fluid Ounces");
    }

    public String getRoutineDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("RoutineData", null);
        Type type = new TypeToken<ArrayList<ExercisesListBean>>() {
        }.getType();
        return json;
    }

    public void saveRoutineData(ArrayList<ExercisesListBean> subOptionsBeans) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(subOptionsBeans);
        editor.putString("RoutineData", json);
        editor.apply();
        editor.commit();
    }

    public void cleanRoutinedata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("RoutineData", null);
        editor.apply();
    }

    public String getUpdateRoutineDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("UpdateRoutineData", null);
        return json;
    }

    public void saveUpdateRoutineData(RoutinePlanDetailsBean subOptionsBeans) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(subOptionsBeans);
        editor.putString("UpdateRoutineData", json);
        editor.apply();
        editor.commit();
    }

    public void cleanUpdateRoutinedata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UpdateRoutineData", null);
        editor.apply();
    }

    public String getRecipeDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("RecipeData", null);
        Type type = new TypeToken<ArrayList<ChompProductBean>>() {
        }.getType();
        return json;
    }

    public void saveRecipeData(ArrayList<ChompProductBean> subOptionsBeans) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(subOptionsBeans);
        editor.putString("RecipeData", json);
        editor.apply();
        editor.commit();
    }

    public void cleanRecipedata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("RecipeData", null);
        editor.apply();
    }

    public String getMealDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("MealData", null);
        return json;
    }

    public void saveMealData(ArrayList<ChompProductBean> subOptionsBeans) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(subOptionsBeans);
        editor.putString("MealData", json);
        editor.apply();
        editor.commit();
    }

    public void cleanMealdata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MealData", null);
        editor.apply();
    }

    public String getIsUpdatePlan() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("updatePlanId", "");
    }

    public void setIsUpdatePlan(String planId) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("updatePlanId", planId);
        editor.apply();
    }

    public void cleanUpdatePlan() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("updatePlanId", null);
        editor.apply();
    }

    public String getRoutineHeaderDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("routinePrefBean", null);
        return json;
    }

    public void saveRoutinHeadereData(RoutinePrefBean routinePrefBean) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(routinePrefBean);
        editor.putString("routinePrefBean", json);
        editor.apply();
        editor.commit();
    }

    public void cleanRoutineHeaderedata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("routinePrefBean", null);
        editor.apply();
    }

    public String getMealNameDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("mealPrefBean", null);
        return json;
    }

    public void saveMealNameData(String recipeName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mealPrefBean", recipeName);
        editor.apply();
    }

    public void cleanMealNamedata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mealPrefBean", null);
        editor.apply();
    }

    public String getMealDateDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("mealDatePrefBean", null);
        return json;
    }

    public void saveMealDateData(String recipeName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mealDatePrefBean", recipeName);
        editor.apply();
    }

    public void cleanMealDatedata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mealDatePrefBean", null);
        editor.apply();
    }

    public String getMealIDDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("mealPrefId", null);
        return json;
    }

    public void saveMealIDData(String recipeId) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mealPrefId", recipeId);
        editor.apply();
    }

    public void cleanMealIDdata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mealPrefId", null);
        editor.apply();
    }

    public String isUpdateMeal() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("updateMeal", null);
        return json;
    }

    public void saveUpdateMeal(String recipeId) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("updateMeal", recipeId);
        editor.apply();
    }

    public void cleanUpdateMeal() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("updateMeal", null);
        editor.apply();
    }

    public String getCountyPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("countryPref", null);
        return json;
    }

    public void saveCountyData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("countryPref", countryName);
        editor.apply();
    }

    public void cleanCountydata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("countryPref", null);
        editor.apply();
    }

    public String getPrivacyPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("privacyPref", "0");
        return json;
    }

    public void savePrivacyData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("privacyPref", countryName);
        editor.apply();
    }

    public void cleanPrivacydata() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("privacyPref", null);
        editor.apply();
    }

    public String getMyFoodListPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("myFoodPref", "50");
        return json;
    }

    public void saveMyFoodListData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("myFoodPref", countryName);
        editor.apply();
    }

    public String getScreenOnPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("screenOnPref", "1");
        return json;
    }

    public void saveScreenOnData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("screenOnPref", countryName);
        editor.apply();
    }

    public String getLookScreenPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("lookScreenPref", "1");
        return json;
    }

    public void saveLookScreenData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lookScreenPref", countryName);
        editor.apply();
    }

    public String getSoundPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("soundPref", "1");
        return json;
    }

    public void saveSoundData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("soundPref", countryName);
        editor.apply();
    }

    public String getVibratePref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("vibratePref", "1");
        return json;
    }

    public void saveVibrateData(String countryName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("vibratePref", countryName);
        editor.apply();
    }

    public String getBurnedCaloriesPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        String json = preferences.getString("burnedCalories", "0");
        return json;
    }

    public void saveBurnedCaloriesData(String burnedCalories) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("burnedCalories", burnedCalories);
        editor.apply();
    }
}
