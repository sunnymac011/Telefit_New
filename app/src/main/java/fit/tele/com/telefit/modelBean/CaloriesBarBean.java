
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CaloriesBarBean implements Parcelable {

    @SerializedName("Monday")
    @Expose
    public MainCaloriesBean monday;
    @SerializedName("Tuesday")
    @Expose
    public MainCaloriesBean tuesday;
    @SerializedName("Wednesday")
    @Expose
    public MainCaloriesBean wednesday;
    @SerializedName("Thursday")
    @Expose
    public MainCaloriesBean thursday;
    @SerializedName("Friday")
    @Expose
    public MainCaloriesBean friday;
    @SerializedName("Saturday")
    @Expose
    public MainCaloriesBean saturday;
    @SerializedName("Sunday")
    @Expose
    public MainCaloriesBean sunday;
    @SerializedName("goal")
    @Expose
    public ArrayList<GoalBean> goal = null;

    public CaloriesBarBean() {
    }

    protected CaloriesBarBean(Parcel in) {
        monday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        tuesday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        wednesday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        thursday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        friday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        saturday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        sunday = in.readParcelable(MainCaloriesBean.class.getClassLoader());
        goal = in.createTypedArrayList(GoalBean.CREATOR);
    }

    public static final Creator<CaloriesBarBean> CREATOR = new Creator<CaloriesBarBean>() {
        @Override
        public CaloriesBarBean createFromParcel(Parcel in) {
            return new CaloriesBarBean(in);
        }

        @Override
        public CaloriesBarBean[] newArray(int size) {
            return new CaloriesBarBean[size];
        }
    };

    public MainCaloriesBean getMonday() {
        return monday;
    }

    public void setMonday(MainCaloriesBean monday) {
        this.monday = monday;
    }

    public MainCaloriesBean getTuesday() {
        return tuesday;
    }

    public void setTuesday(MainCaloriesBean tuesday) {
        this.tuesday = tuesday;
    }

    public MainCaloriesBean getWednesday() {
        return wednesday;
    }

    public void setWednesday(MainCaloriesBean wednesday) {
        this.wednesday = wednesday;
    }

    public MainCaloriesBean getThursday() {
        return thursday;
    }

    public void setThursday(MainCaloriesBean thursday) {
        this.thursday = thursday;
    }

    public MainCaloriesBean getFriday() {
        return friday;
    }

    public void setFriday(MainCaloriesBean friday) {
        this.friday = friday;
    }

    public MainCaloriesBean getSaturday() {
        return saturday;
    }

    public void setSaturday(MainCaloriesBean saturday) {
        this.saturday = saturday;
    }

    public MainCaloriesBean getSunday() {
        return sunday;
    }

    public void setSunday(MainCaloriesBean sunday) {
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
        return "CaloriesBarBean{" +
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
