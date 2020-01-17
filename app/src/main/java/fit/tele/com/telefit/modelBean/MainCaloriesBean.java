
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCaloriesBean implements Parcelable {

    @SerializedName("total_calories")
    @Expose
    private Float totalCalories;
    @SerializedName("fitbit_calories")
    @Expose
    private Float fitbitCalories;

    protected MainCaloriesBean(Parcel in) {
        if (in.readByte() == 0) {
            totalCalories = null;
        } else {
            totalCalories = in.readFloat();
        }
        if (in.readByte() == 0) {
            fitbitCalories = null;
        } else {
            fitbitCalories = in.readFloat();
        }
    }

    public static final Creator<MainCaloriesBean> CREATOR = new Creator<MainCaloriesBean>() {
        @Override
        public MainCaloriesBean createFromParcel(Parcel in) {
            return new MainCaloriesBean(in);
        }

        @Override
        public MainCaloriesBean[] newArray(int size) {
            return new MainCaloriesBean[size];
        }
    };

    public Float getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public Float getFitbitCalories() {
        return fitbitCalories;
    }

    public void setFitbitCalories(Float fitbitCalories) {
        this.fitbitCalories = fitbitCalories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (totalCalories == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(totalCalories);
        }
        if (fitbitCalories == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(fitbitCalories);
        }
    }

    @Override
    public String toString() {
        return "MainCaloriesBean{" +
                "totalCalories=" + totalCalories +
                ", fitbitCalories=" + fitbitCalories +
                '}';
    }
}
