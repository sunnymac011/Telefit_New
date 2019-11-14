
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;

public class NewRecipeBean implements Parcelable {

    @SerializedName("food_cat_id")
    @Expose
    private String recipe_id;
    @SerializedName("recipe_name")
    @Expose
    private String recipeName;
    @SerializedName("food_type")
    @Expose
    private String foodType;
    @SerializedName("is_racipe_meal")
    @Expose
    private String isRacipeMeal;
    @SerializedName("meals_date")
    @Expose
    private String mealDate;
    @SerializedName("racipe_calories")
    @Expose
    private String racipeCalories;
    @SerializedName("meals_id")
    @Expose
    private String mealsId;
    @SerializedName("food")
    @Expose
    private ArrayList<ChompProductBean> food = null;

    public NewRecipeBean() {
    }

    protected NewRecipeBean(Parcel in) {
        recipe_id = in.readString();
        recipeName = in.readString();
        foodType = in.readString();
        isRacipeMeal = in.readString();
        mealDate = in.readString();
        racipeCalories = in.readString();
        mealsId = in.readString();
        food = in.createTypedArrayList(ChompProductBean.CREATOR);
    }

    public static final Creator<NewRecipeBean> CREATOR = new Creator<NewRecipeBean>() {
        @Override
        public NewRecipeBean createFromParcel(Parcel in) {
            return new NewRecipeBean(in);
        }

        @Override
        public NewRecipeBean[] newArray(int size) {
            return new NewRecipeBean[size];
        }
    };

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getIsRacipeMeal() {
        return isRacipeMeal;
    }

    public void setIsRacipeMeal(String isRacipeMeal) {
        this.isRacipeMeal = isRacipeMeal;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public String getRacipeCalories() {
        return racipeCalories;
    }

    public void setRacipeCalories(String racipeCalories) {
        this.racipeCalories = racipeCalories;
    }

    public String getMealsId() {
        return mealsId;
    }

    public void setMealsId(String mealsId) {
        this.mealsId = mealsId;
    }

    public ArrayList<ChompProductBean> getFood() {
        return food;
    }

    public void setFood(ArrayList<ChompProductBean> food) {
        this.food = food;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipe_id);
        dest.writeString(recipeName);
        dest.writeString(foodType);
        dest.writeString(isRacipeMeal);
        dest.writeString(mealDate);
        dest.writeString(racipeCalories);
        dest.writeString(mealsId);
        dest.writeTypedList(food);
    }

    @Override
    public String toString() {
        return "NewRecipeBean{" +
                "recipe_id='" + recipe_id + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", foodType='" + foodType + '\'' +
                ", isRacipeMeal='" + isRacipeMeal + '\'' +
                ", mealDate='" + mealDate + '\'' +
                ", racipeCalories='" + racipeCalories + '\'' +
                ", mealsId='" + mealsId + '\'' +
                ", food=" + food +
                '}';
    }
}
