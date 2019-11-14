
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fat implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("measurement")
    @Expose
    private String measurement;
    @SerializedName("per_100g")
    @Expose
    private String per100g;
    @SerializedName("per_serving")
    @Expose
    private String perServing;

    public Fat() {
    }

    protected Fat(Parcel in) {
        name = in.readString();
        measurement = in.readString();
        per100g = in.readString();
        perServing = in.readString();
    }

    public static final Creator<Fat> CREATOR = new Creator<Fat>() {
        @Override
        public Fat createFromParcel(Parcel in) {
            return new Fat(in);
        }

        @Override
        public Fat[] newArray(int size) {
            return new Fat[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getPer100g() {
        return per100g;
    }

    public void setPer100g(String per100g) {
        this.per100g = per100g;
    }

    public String getPerServing() {
        return perServing;
    }

    public void setPerServing(String perServing) {
        this.perServing = perServing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(measurement);
        dest.writeString(per100g);
        dest.writeString(perServing);
    }

    @Override
    public String toString() {
        return "Fat{" +
                "name='" + name + '\'' +
                ", measurement='" + measurement + '\'' +
                ", per100g='" + per100g + '\'' +
                ", perServing='" + perServing + '\'' +
                '}';
    }
}
