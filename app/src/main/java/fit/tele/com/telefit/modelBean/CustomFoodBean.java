
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import fit.tele.com.telefit.modelBean.chompBeans.NutritionLabel;

public class CustomFoodBean implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("ingredients")
    @Expose
    private String ingredients;
    @SerializedName("package_size")
    @Expose
    private String packageSize;
    @SerializedName("serving_size")
    @Expose
    private String servingSize;
    @SerializedName("nutrition_label")
    @Expose
    private NutritionLabel nutritionLabel;

    public CustomFoodBean() {
    }

    protected CustomFoodBean(Parcel in) {
        name = in.readString();
        manufacturer = in.readString();
        barcode = in.readString();
        ingredients = in.readString();
        packageSize = in.readString();
        servingSize = in.readString();
        nutritionLabel = in.readParcelable(NutritionLabel.class.getClassLoader());
    }

    public static final Creator<CustomFoodBean> CREATOR = new Creator<CustomFoodBean>() {
        @Override
        public CustomFoodBean createFromParcel(Parcel in) {
            return new CustomFoodBean(in);
        }

        @Override
        public CustomFoodBean[] newArray(int size) {
            return new CustomFoodBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public NutritionLabel getNutritionLabel() {
        return nutritionLabel;
    }

    public void setNutritionLabel(NutritionLabel nutritionLabel) {
        this.nutritionLabel = nutritionLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(manufacturer);
        dest.writeString(barcode);
        dest.writeString(ingredients);
        dest.writeString(packageSize);
        dest.writeString(servingSize);
        dest.writeParcelable(nutritionLabel, flags);
    }

    @Override
    public String toString() {
        return "CustomFoodBean{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", barcode='" + barcode + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", packageSize='" + packageSize + '\'' +
                ", servingSize='" + servingSize + '\'' +
                ", nutritionLabel=" + nutritionLabel +
                '}';
    }
}
