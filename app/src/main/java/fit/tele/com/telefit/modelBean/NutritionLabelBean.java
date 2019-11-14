package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutritionLabelBean implements Parcelable {

    @SerializedName("nutrition_type")
    @Expose
    private String nutritionType;
    @SerializedName("measurement")
    @Expose
    private String measurement;
    @SerializedName("per_100g")
    @Expose
    private String per100g;
    @SerializedName("per_serving")
    @Expose
    private String perServing;

    public NutritionLabelBean() {
    }

    protected NutritionLabelBean(Parcel in) {
        nutritionType = in.readString();
        measurement = in.readString();
        per100g = in.readString();
        perServing = in.readString();
    }

    public static final Creator<NutritionLabelBean> CREATOR = new Creator<NutritionLabelBean>() {
        @Override
        public NutritionLabelBean createFromParcel(Parcel in) {
            return new NutritionLabelBean(in);
        }

        @Override
        public NutritionLabelBean[] newArray(int size) {
            return new NutritionLabelBean[size];
        }
    };

    public String getNutritionType() {
        return nutritionType;
    }

    public void setNutritionType(String nutritionType) {
        this.nutritionType = nutritionType;
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
        dest.writeString(nutritionType);
        dest.writeString(measurement);
        dest.writeString(per100g);
        dest.writeString(perServing);
    }

    @Override
    public String toString() {
        return "NutritionLabelBean{" +
                "nutritionType='" + nutritionType + '\'' +
                ", measurement='" + measurement + '\'' +
                ", per100g='" + per100g + '\'' +
                ", perServing='" + perServing + '\'' +
                '}';
    }
}
