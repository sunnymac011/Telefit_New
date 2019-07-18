
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Countries implements Parcelable {

    @SerializedName("united-states")
    @Expose
    private String unitedStates;

    protected Countries(Parcel in) {
        unitedStates = in.readString();
    }

    public static final Creator<Countries> CREATOR = new Creator<Countries>() {
        @Override
        public Countries createFromParcel(Parcel in) {
            return new Countries(in);
        }

        @Override
        public Countries[] newArray(int size) {
            return new Countries[size];
        }
    };

    public String getUnitedStates() {
        return unitedStates;
    }

    public void setUnitedStates(String unitedStates) {
        this.unitedStates = unitedStates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(unitedStates);
    }

    @Override
    public String toString() {
        return "Countries{" +
                "unitedStates='" + unitedStates + '\'' +
                '}';
    }
}
