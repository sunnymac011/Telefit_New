
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoalDataBean implements Parcelable {

    @SerializedName("Monday")
    @Expose
    public float monday;
    @SerializedName("Tuesday")
    @Expose
    public float tuesday;
    @SerializedName("Wednesday")
    @Expose
    public float wednesday;
    @SerializedName("Thursday")
    @Expose
    public float thursday;
    @SerializedName("Friday")
    @Expose
    public float friday;
    @SerializedName("Saturday")
    @Expose
    public float saturday;
    @SerializedName("Sunday")
    @Expose
    public float sunday;

    public GoalDataBean() {
    }

    protected GoalDataBean(Parcel in) {
        monday = in.readFloat();
        tuesday = in.readFloat();
        wednesday = in.readFloat();
        thursday = in.readFloat();
        friday = in.readFloat();
        saturday = in.readFloat();
        sunday = in.readFloat();
    }

    public static final Creator<GoalDataBean> CREATOR = new Creator<GoalDataBean>() {
        @Override
        public GoalDataBean createFromParcel(Parcel in) {
            return new GoalDataBean(in);
        }

        @Override
        public GoalDataBean[] newArray(int size) {
            return new GoalDataBean[size];
        }
    };

    public float getMonday() {
        return monday;
    }

    public void setMonday(float monday) {
        this.monday = monday;
    }

    public float getTuesday() {
        return tuesday;
    }

    public void setTuesday(float tuesday) {
        this.tuesday = tuesday;
    }

    public float getWednesday() {
        return wednesday;
    }

    public void setWednesday(float wednesday) {
        this.wednesday = wednesday;
    }

    public float getThursday() {
        return thursday;
    }

    public void setThursday(float thursday) {
        this.thursday = thursday;
    }

    public float getFriday() {
        return friday;
    }

    public void setFriday(float friday) {
        this.friday = friday;
    }

    public float getSaturday() {
        return saturday;
    }

    public void setSaturday(float saturday) {
        this.saturday = saturday;
    }

    public float getSunday() {
        return sunday;
    }

    public void setSunday(float sunday) {
        this.sunday = sunday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(monday);
        dest.writeFloat(tuesday);
        dest.writeFloat(wednesday);
        dest.writeFloat(thursday);
        dest.writeFloat(friday);
        dest.writeFloat(saturday);
        dest.writeFloat(sunday);
    }

    @Override
    public String toString() {
        return "GoalDataBean{" +
                "monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                '}';
    }
}
