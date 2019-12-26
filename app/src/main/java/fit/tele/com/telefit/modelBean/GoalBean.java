
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalBean implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("protein")
    @Expose
    public String protein;
    @SerializedName("carbs")
    @Expose
    public String carbs;
    @SerializedName("fat")
    @Expose
    public String fat;
    @SerializedName("bmi")
    @Expose
    public String bmi;
    @SerializedName("body_fat")
    @Expose
    public String bodyFat;
    @SerializedName("cholesterol")
    @Expose
    public String cholesterol;
    @SerializedName("fiber")
    @Expose
    public String fiber;
    @SerializedName("water")
    @Expose
    public String water;
    @SerializedName("weight")
    @Expose
    public String weight;
    @SerializedName("weight_type")
    @Expose
    public String weightType;
    @SerializedName("goal_body_fat")
    @Expose
    public String goalBodyFat;
    @SerializedName("goal_water")
    @Expose
    public String goalWater;
    @SerializedName("goal_weight")
    @Expose
    public String goalWeight;
    @SerializedName("is_delete")
    @Expose
    public String isDelete;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("goal_day_date")
    @Expose
    public String goalDate;
    @SerializedName("meal_protein")
    @Expose
    public String mealProtein;
    @SerializedName("meal_carbs")
    @Expose
    public String mealCarbs;
    @SerializedName("meal_fat")
    @Expose
    public String mealFat;
    @SerializedName("meal_cholesterol")
    @Expose
    public String mealCholesterol;
    @SerializedName("meal_fiber")
    @Expose
    public String mealFiber;

    protected GoalBean(Parcel in) {
        id = in.readString();
        userId = in.readString();
        protein = in.readString();
        carbs = in.readString();
        fat = in.readString();
        bmi = in.readString();
        bodyFat = in.readString();
        cholesterol = in.readString();
        fiber = in.readString();
        water = in.readString();
        weight = in.readString();
        weightType = in.readString();
        goalBodyFat = in.readString();
        goalWater = in.readString();
        goalWeight = in.readString();
        isDelete = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        goalDate = in.readString();
        mealProtein = in.readString();
        mealCarbs = in.readString();
        mealFat = in.readString();
        mealCholesterol = in.readString();
        mealFiber = in.readString();
    }

    public static final Creator<GoalBean> CREATOR = new Creator<GoalBean>() {
        @Override
        public GoalBean createFromParcel(Parcel in) {
            return new GoalBean(in);
        }

        @Override
        public GoalBean[] newArray(int size) {
            return new GoalBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getGoalBodyFat() {
        return goalBodyFat;
    }

    public void setGoalBodyFat(String goalBodyFat) {
        this.goalBodyFat = goalBodyFat;
    }

    public String getGoalWater() {
        return goalWater;
    }

    public void setGoalWater(String goalWater) {
        this.goalWater = goalWater;
    }

    public String getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(String goalWeight) {
        this.goalWeight = goalWeight;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public String getMealProtein() {
        return mealProtein;
    }

    public void setMealProtein(String mealProtein) {
        this.mealProtein = mealProtein;
    }

    public String getMealCarbs() {
        return mealCarbs;
    }

    public void setMealCarbs(String mealCarbs) {
        this.mealCarbs = mealCarbs;
    }

    public String getMealFat() {
        return mealFat;
    }

    public void setMealFat(String mealFat) {
        this.mealFat = mealFat;
    }

    public String getMealCholesterol() {
        return mealCholesterol;
    }

    public void setMealCholesterol(String mealCholesterol) {
        this.mealCholesterol = mealCholesterol;
    }

    public String getMealFiber() {
        return mealFiber;
    }

    public void setMealFiber(String mealFiber) {
        this.mealFiber = mealFiber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(protein);
        dest.writeString(carbs);
        dest.writeString(fat);
        dest.writeString(bmi);
        dest.writeString(bodyFat);
        dest.writeString(cholesterol);
        dest.writeString(fiber);
        dest.writeString(water);
        dest.writeString(weight);
        dest.writeString(weightType);
        dest.writeString(goalBodyFat);
        dest.writeString(goalWater);
        dest.writeString(goalWeight);
        dest.writeString(isDelete);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(goalDate);
        dest.writeString(mealProtein);
        dest.writeString(mealCarbs);
        dest.writeString(mealFat);
        dest.writeString(mealCholesterol);
        dest.writeString(mealFiber);
    }

    @Override
    public String toString() {
        return "GoalBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", protein='" + protein + '\'' +
                ", carbs='" + carbs + '\'' +
                ", fat='" + fat + '\'' +
                ", bmi='" + bmi + '\'' +
                ", bodyFat='" + bodyFat + '\'' +
                ", cholesterol='" + cholesterol + '\'' +
                ", fiber='" + fiber + '\'' +
                ", water='" + water + '\'' +
                ", weight='" + weight + '\'' +
                ", weightType='" + weightType + '\'' +
                ", goalBodyFat='" + goalBodyFat + '\'' +
                ", goalWater='" + goalWater + '\'' +
                ", goalWeight='" + goalWeight + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", goalDate='" + goalDate + '\'' +
                ", mealProtein='" + mealProtein + '\'' +
                ", mealCarbs='" + mealCarbs + '\'' +
                ", mealFat='" + mealFat + '\'' +
                ", mealCholesterol='" + mealCholesterol + '\'' +
                ", mealFiber='" + mealFiber + '\'' +
                '}';
    }
}
