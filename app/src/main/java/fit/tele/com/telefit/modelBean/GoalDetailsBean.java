
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoalDetailsBean implements Parcelable {

    @SerializedName("Monday")
    @Expose
    public GoalBean goalMonday;
    @SerializedName("Tuesday")
    @Expose
    public GoalBean goalTuesday;
    @SerializedName("Wednesday")
    @Expose
    public GoalBean goalWednesday;
    @SerializedName("Thursday")
    @Expose
    public GoalBean goalThursday;
    @SerializedName("Friday")
    @Expose
    public GoalBean goalFriday;
    @SerializedName("Saturday")
    @Expose
    public GoalBean goalSaturday;
    @SerializedName("Sunday")
    @Expose
    public GoalBean goalSunday;

    protected GoalDetailsBean(Parcel in) {
        goalMonday = in.readParcelable(GoalBean.class.getClassLoader());
        goalTuesday = in.readParcelable(GoalBean.class.getClassLoader());
        goalWednesday = in.readParcelable(GoalBean.class.getClassLoader());
        goalThursday = in.readParcelable(GoalBean.class.getClassLoader());
        goalFriday = in.readParcelable(GoalBean.class.getClassLoader());
        goalSaturday = in.readParcelable(GoalBean.class.getClassLoader());
        goalSunday = in.readParcelable(GoalBean.class.getClassLoader());
    }

    public static final Creator<GoalDetailsBean> CREATOR = new Creator<GoalDetailsBean>() {
        @Override
        public GoalDetailsBean createFromParcel(Parcel in) {
            return new GoalDetailsBean(in);
        }

        @Override
        public GoalDetailsBean[] newArray(int size) {
            return new GoalDetailsBean[size];
        }
    };

    public GoalBean getGoalMonday() {
        return goalMonday;
    }

    public void setGoalMonday(GoalBean goalMonday) {
        this.goalMonday = goalMonday;
    }

    public GoalBean getGoalTuesday() {
        return goalTuesday;
    }

    public void setGoalTuesday(GoalBean goalTuesday) {
        this.goalTuesday = goalTuesday;
    }

    public GoalBean getGoalWednesday() {
        return goalWednesday;
    }

    public void setGoalWednesday(GoalBean goalWednesday) {
        this.goalWednesday = goalWednesday;
    }

    public GoalBean getGoalThursday() {
        return goalThursday;
    }

    public void setGoalThursday(GoalBean goalThursday) {
        this.goalThursday = goalThursday;
    }

    public GoalBean getGoalFriday() {
        return goalFriday;
    }

    public void setGoalFriday(GoalBean goalFriday) {
        this.goalFriday = goalFriday;
    }

    public GoalBean getGoalSaturday() {
        return goalSaturday;
    }

    public void setGoalSaturday(GoalBean goalSaturday) {
        this.goalSaturday = goalSaturday;
    }

    public GoalBean getGoalSunday() {
        return goalSunday;
    }

    public void setGoalSunday(GoalBean goalSunday) {
        this.goalSunday = goalSunday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(goalMonday, flags);
        dest.writeParcelable(goalTuesday, flags);
        dest.writeParcelable(goalWednesday, flags);
        dest.writeParcelable(goalThursday, flags);
        dest.writeParcelable(goalFriday, flags);
        dest.writeParcelable(goalSaturday, flags);
        dest.writeParcelable(goalSunday, flags);
    }

    @Override
    public String toString() {
        return "GoalDetailsBean{" +
                "goalMonday=" + goalMonday +
                ", goalTuesday=" + goalTuesday +
                ", goalWednesday=" + goalWednesday +
                ", goalThursday=" + goalThursday +
                ", goalFriday=" + goalFriday +
                ", goalSaturday=" + goalSaturday +
                ", goalSunday=" + goalSunday +
                '}';
    }
}
