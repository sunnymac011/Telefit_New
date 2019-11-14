
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalBarBean implements Parcelable {

    @SerializedName("1st week")
    @Expose
    public GoalBean one_week;
    @SerializedName("2nd week")
    @Expose
    public GoalBean two_week;
    @SerializedName("3rd week")
    @Expose
    public GoalBean three_week;
    @SerializedName("4th week")
    @Expose
    public GoalBean four_week;
    @SerializedName("5th week")
    @Expose
    public GoalBean five_week;

    protected GoalBarBean(Parcel in) {
        one_week = in.readParcelable(GoalBean.class.getClassLoader());
        two_week = in.readParcelable(GoalBean.class.getClassLoader());
        three_week = in.readParcelable(GoalBean.class.getClassLoader());
        four_week = in.readParcelable(GoalBean.class.getClassLoader());
        five_week = in.readParcelable(GoalBean.class.getClassLoader());
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

    public GoalBean getOne_week() {
        return one_week;
    }

    public void setOne_week(GoalBean one_week) {
        this.one_week = one_week;
    }

    public GoalBean getTwo_week() {
        return two_week;
    }

    public void setTwo_week(GoalBean two_week) {
        this.two_week = two_week;
    }

    public GoalBean getThree_week() {
        return three_week;
    }

    public void setThree_week(GoalBean three_week) {
        this.three_week = three_week;
    }

    public GoalBean getFour_week() {
        return four_week;
    }

    public void setFour_week(GoalBean four_week) {
        this.four_week = four_week;
    }

    public GoalBean getFive_week() {
        return five_week;
    }

    public void setFive_week(GoalBean five_week) {
        this.five_week = five_week;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(one_week, flags);
        dest.writeParcelable(two_week, flags);
        dest.writeParcelable(three_week, flags);
        dest.writeParcelable(four_week, flags);
        dest.writeParcelable(five_week, flags);
    }

    @Override
    public String toString() {
        return "GoalBarBean{" +
                "one_week=" + one_week +
                ", two_week=" + two_week +
                ", three_week=" + three_week +
                ", four_week=" + four_week +
                ", five_week=" + five_week +
                '}';
    }
}
