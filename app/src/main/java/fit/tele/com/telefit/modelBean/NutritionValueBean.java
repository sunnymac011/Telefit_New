

package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutritionValueBean implements Parcelable {

    @SerializedName("carbohydrates")
    @Expose
    public float carbohydrates;
    @SerializedName("fat")
    @Expose
    public float fat;
    @SerializedName("proteins")
    @Expose
    public float proteins;

    protected NutritionValueBean(Parcel in) {
        carbohydrates = in.readFloat();
        fat = in.readFloat();
        proteins = in.readFloat();
    }

    public static final Creator<NutritionValueBean> CREATOR = new Creator<NutritionValueBean>() {
        @Override
        public NutritionValueBean createFromParcel(Parcel in) {
            return new NutritionValueBean(in);
        }

        @Override
        public NutritionValueBean[] newArray(int size) {
            return new NutritionValueBean[size];
        }
    };

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(carbohydrates);
        dest.writeFloat(fat);
        dest.writeFloat(proteins);
    }

    @Override
    public String toString() {
        return "NutritionValueBean{" +
                "carbohydrates=" + carbohydrates +
                ", fat=" + fat +
                ", proteins=" + proteins +
                '}';
    }
}
