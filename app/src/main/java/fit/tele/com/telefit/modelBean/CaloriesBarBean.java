
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CaloriesBarBean implements Parcelable {

    @SerializedName("Monday")
    @Expose
    public Float monday;
    @SerializedName("Tuesday")
    @Expose
    public Float tuesday;
    @SerializedName("Wednesday")
    @Expose
    public Float wednesday;
    @SerializedName("Thursday")
    @Expose
    public Float thursday;
    @SerializedName("Friday")
    @Expose
    public Float friday;
    @SerializedName("Saturday")
    @Expose
    public Float saturday;
    @SerializedName("Sunday")
    @Expose
    public Float sunday;
    @SerializedName("goal")
    @Expose
    public ArrayList<GoalBean> goal = null;

    protected CaloriesBarBean(Parcel in) {
        if (in.readByte() == 0) {
            monday = null;
        } else {
            monday = in.readFloat();
        }
        if (in.readByte() == 0) {
            tuesday = null;
        } else {
            tuesday = in.readFloat();
        }
        if (in.readByte() == 0) {
            wednesday = null;
        } else {
            wednesday = in.readFloat();
        }
        if (in.readByte() == 0) {
            thursday = null;
        } else {
            thursday = in.readFloat();
        }
        if (in.readByte() == 0) {
            friday = null;
        } else {
            friday = in.readFloat();
        }
        if (in.readByte() == 0) {
            saturday = null;
        } else {
            saturday = in.readFloat();
        }
        if (in.readByte() == 0) {
            sunday = null;
        } else {
            sunday = in.readFloat();
        }
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

    public Float getMonday() {
        return (float) (monday/4.184);
    }

    public void setMonday(Float monday) {
        this.monday = monday;
    }

    public Float getTuesday() {
        return (float) (tuesday/4.184);
    }

    public void setTuesday(Float tuesday) {
        this.tuesday = tuesday;
    }

    public Float getWednesday() {
        return (float) (wednesday/4.184);
    }

    public void setWednesday(Float wednesday) {
        this.wednesday = wednesday;
    }

    public Float getThursday() {
        return (float) (thursday/4.184);
    }

    public void setThursday(Float thursday) {
        this.thursday = thursday;
    }

    public Float getFriday() {
        return (float) (friday/4.184);
    }

    public void setFriday(Float friday) {
        this.friday = friday;
    }

    public Float getSaturday() {
        return (float) (saturday/4.184);
    }

    public void setSaturday(Float saturday) {
        this.saturday = saturday;
    }

    public Float getSunday() {
        return (float) (sunday/4.184);
    }

    public void setSunday(Float sunday) {
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
        if (monday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(monday);
        }
        if (tuesday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(tuesday);
        }
        if (wednesday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(wednesday);
        }
        if (thursday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(thursday);
        }
        if (friday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(friday);
        }
        if (saturday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(saturday);
        }
        if (sunday == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(sunday);
        }
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
