
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NutritionBarBean implements Parcelable {

    @SerializedName("Sunday")
    @Expose
    public NutritionValueBean sunday;
    @SerializedName("Monday")
    @Expose
    public NutritionValueBean monday;
    @SerializedName("Tuesday")
    @Expose
    public NutritionValueBean tuesday;
    @SerializedName("Wednesday")
    @Expose
    public NutritionValueBean wednesday;
    @SerializedName("Thursday")
    @Expose
    public NutritionValueBean thursday;
    @SerializedName("Friday")
    @Expose
    public NutritionValueBean friday;
    @SerializedName("Saturday")
    @Expose
    public NutritionValueBean saturday;
    @SerializedName("goal")
    @Expose
    public ArrayList<GoalBean> goal;

    protected NutritionBarBean(Parcel in) {
        monday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        tuesday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        wednesday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        thursday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        friday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        saturday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        sunday = in.readParcelable(NutritionValueBean.class.getClassLoader());
        goal = in.createTypedArrayList(GoalBean.CREATOR);
    }

    public static final Creator<NutritionBarBean> CREATOR = new Creator<NutritionBarBean>() {
        @Override
        public NutritionBarBean createFromParcel(Parcel in) {
            return new NutritionBarBean(in);
        }

        @Override
        public NutritionBarBean[] newArray(int size) {
            return new NutritionBarBean[size];
        }
    };

    public NutritionValueBean getMonday() {
        return monday;
    }

    public void setMonday(NutritionValueBean monday) {
        this.monday = monday;
    }

    public NutritionValueBean getTuesday() {
        return tuesday;
    }

    public void setTuesday(NutritionValueBean tuesday) {
        this.tuesday = tuesday;
    }

    public NutritionValueBean getWednesday() {
        return wednesday;
    }

    public void setWednesday(NutritionValueBean wednesday) {
        this.wednesday = wednesday;
    }

    public NutritionValueBean getThursday() {
        return thursday;
    }

    public void setThursday(NutritionValueBean thursday) {
        this.thursday = thursday;
    }

    public NutritionValueBean getFriday() {
        return friday;
    }

    public void setFriday(NutritionValueBean friday) {
        this.friday = friday;
    }

    public NutritionValueBean getSaturday() {
        return saturday;
    }

    public void setSaturday(NutritionValueBean saturday) {
        this.saturday = saturday;
    }

    public NutritionValueBean getSunday() {
        return sunday;
    }

    public void setSunday(NutritionValueBean sunday) {
        this.sunday = sunday;
    }

    public ArrayList<GoalBean> getGoal() {
        return goal;
    }

    public void setGoal(ArrayList<GoalBean> goal) {
        this.goal = goal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(monday, flags);
        dest.writeParcelable(tuesday, flags);
        dest.writeParcelable(wednesday, flags);
        dest.writeParcelable(thursday, flags);
        dest.writeParcelable(friday, flags);
        dest.writeParcelable(saturday, flags);
        dest.writeParcelable(sunday, flags);
        dest.writeTypedList(goal);
    }

    @Override
    public String toString() {
        return "NutritionBarBean{" +
                "monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", goal=" + goal +
                '}';
    }
}
