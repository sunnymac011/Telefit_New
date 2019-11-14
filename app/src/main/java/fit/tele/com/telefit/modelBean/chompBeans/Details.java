
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("upc")
    @Expose
    private String upc;
    @SerializedName("brand")
    @Expose
    private String brand;
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
    @SerializedName("images")
    @Expose
    private Images images;

    protected Details(Parcel in) {
        name = in.readString();
        barcode = in.readString();
        upc = in.readString();
        brand = in.readString();
        ingredients = in.readString();
        packageSize = in.readString();
        servingSize = in.readString();
        nutritionLabel = in.readParcelable(NutritionLabel.class.getClassLoader());
        images = in.readParcelable(Images.class.getClassLoader());
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(barcode);
        dest.writeString(upc);
        dest.writeString(brand);
        dest.writeString(ingredients);
        dest.writeString(packageSize);
        dest.writeString(servingSize);
        dest.writeParcelable(nutritionLabel, flags);
        dest.writeParcelable(images, flags);
    }

    @Override
    public String toString() {
        return "Details{" +
                "name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", upc='" + upc + '\'' +
                ", brand='" + brand + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", packageSize='" + packageSize + '\'' +
                ", servingSize='" + servingSize + '\'' +
                ", nutritionLabel=" + nutritionLabel +
                ", images=" + images +
                '}';
    }
}
