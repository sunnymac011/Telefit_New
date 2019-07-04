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
import fit.tele.com.telefit.modelBean.LoginBean;
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
}
