
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoalBarBean implements Parcelable {

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
    @SerializedName("cholesterol")
    @Expose
    public String cholesterol;
    @SerializedName("fiber")
    @Expose
    public String fiber;
    @SerializedName("goal_body_fat")
    @Expose
    public String goalBodyFat;
    @SerializedName("goal_water")
    @Expose
    public String goalWater;
    @SerializedName("goal_weight")
    @Expose
    public String goalWeight;
    @SerializedName("goal_details")
    @Expose
    public GoalDetailsBean goalDetails;

    protected GoalBarBean(Parcel in) {
        protein = in.readString();
        carbs = in.readString();
        fat = in.readString();
        bmi = in.readString();
        cholesterol = in.readString();
        fiber = in.readString();
        goalBodyFat = in.readString();
        goalWater = in.readString();
        goalWeight = in.readString();
        goalDetails = in.readParcelable(GoalDetailsBean.class.getClassLoader());
    }

    public static final Creator<GoalBarBean> CREATOR = new Creator<GoalBarBean>() {
        @Override
        public GoalBarBean createFromParcel(Parcel in) {
            return new GoalBarBean(in);
        }

        @Override
        public GoalBarBean[] newArray(int size) {
            return new GoalBarBean[size];
        }
    };

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

    public GoalDetailsBean getGoalDetails() {
        return goalDetails;
    }

    public void setGoalDetails(GoalDetailsBean goalDetails) {
        this.goalDetails = goalDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(protein);
        dest.writeString(carbs);
        dest.writeString(fat);
        dest.writeString(bmi);
        dest.writeString(cholesterol);
        dest.writeString(fiber);
        dest.writeString(goalBodyFat);
        dest.writeString(goalWater);
        dest.writeString(goalWeight);
        dest.writeParcelable(goalDetails, flags);
    }
}
