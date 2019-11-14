
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetsRepsBean implements Parcelable {

    @SerializedName("sets")
    @Expose
    private String sets;
    @SerializedName("reps")
    @Expose
    private String reps;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("weight_type")
    @Expose
    private String weightType;
    @SerializedName("hours_exe")
    @Expose
    private String srHours;
    @SerializedName("min_exe")
    @Expose
    private String srMin;
    @SerializedName("sec_exe")
    @Expose
    private String srSec;

    public SetsRepsBean() {
    }

    protected SetsRepsBean(Parcel in) {
        sets = in.readString();
        reps = in.readString();
        weight = in.readString();
        weightType = in.readString();
        srHours = in.readString();
        srMin = in.readString();
        srSec = in.readString();
    }

    public static final Creator<SetsRepsBean> CREATOR = new Creator<SetsRepsBean>() {
        @Override
        public SetsRepsBean createFromParcel(Parcel in) {
            return new SetsRepsBean(in);
        }

        @Override
        public SetsRepsBean[] newArray(int size) {
            return new SetsRepsBean[size];
        }
    };

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
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

    public String getSrHours() {
        return srHours;
    }

    public void setSrHours(String srHours) {
        this.srHours = srHours;
    }

    public String getSrMin() {
        return srMin;
    }

    public void setSrMin(String srMin) {
        this.srMin = srMin;
    }

    public String getSrSec() {
        return srSec;
    }

    public void setSrSec(String srSec) {
        this.srSec = srSec;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sets);
        dest.writeString(reps);
        dest.writeString(weight);
        dest.writeString(weightType);
        dest.writeString(srHours);
        dest.writeString(srMin);
        dest.writeString(srSec);
    }

    @Override
    public String toString() {
        return "SetsRepsBean{" +
                "sets='" + sets + '\'' +
                ", reps='" + reps + '\'' +
                ", weight='" + weight + '\'' +
                ", weightType='" + weightType + '\'' +
                ", srHours='" + srHours + '\'' +
                ", srMin='" + srMin + '\'' +
                ", srSec='" + srSec + '\'' +
                '}';
    }
}
