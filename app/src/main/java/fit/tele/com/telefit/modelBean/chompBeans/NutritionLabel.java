
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutritionLabel implements Parcelable {

    @SerializedName("calcium")
    @Expose
    private Calcium calcium;
    @SerializedName("carbohydrates")
    @Expose
    private Carbohydrates carbohydrates;
    @SerializedName("cholesterol")
    @Expose
    private Cholesterol cholesterol;
    @SerializedName("calories")
    @Expose
    private Calories calories;
    @SerializedName("fat")
    @Expose
    private Fat fat;
    @SerializedName("fiber")
    @Expose
    private Fiber fiber;
    @SerializedName("iron")
    @Expose
    private Iron iron;
    @SerializedName("proteins")
    @Expose
    private Proteins proteins;
    @SerializedName("salt")
    @Expose
    private Salt salt;
    @SerializedName("saturated-fat")
    @Expose
    private SaturatedFat saturatedFat;
    @SerializedName("sodium")
    @Expose
    private Sodium sodium;
    @SerializedName("sugars")
    @Expose
    private Sugars sugars;
    @SerializedName("trans-fat")
    @Expose
    private TransFat transFat;
    @SerializedName("vitamin-a")
    @Expose
    private VitaminA vitaminA;
    @SerializedName("vitamin-c")
    @Expose
    private VitaminC vitaminC;

    protected NutritionLabel(Parcel in) {
        calcium = in.readParcelable(Calcium.class.getClassLoader());
        carbohydrates = in.readParcelable(Carbohydrates.class.getClassLoader());
        cholesterol = in.readParcelable(Cholesterol.class.getClassLoader());
        calories = in.readParcelable(Calories.class.getClassLoader());
        fat = in.readParcelable(Fat.class.getClassLoader());
        fiber = in.readParcelable(Fiber.class.getClassLoader());
        iron = in.readParcelable(Iron.class.getClassLoader());
        proteins = in.readParcelable(Proteins.class.getClassLoader());
        salt = in.readParcelable(Salt.class.getClassLoader());
        saturatedFat = in.readParcelable(SaturatedFat.class.getClassLoader());
        sodium = in.readParcelable(Sodium.class.getClassLoader());
        sugars = in.readParcelable(Sugars.class.getClassLoader());
        transFat = in.readParcelable(TransFat.class.getClassLoader());
        vitaminA = in.readParcelable(VitaminA.class.getClassLoader());
        vitaminC = in.readParcelable(VitaminC.class.getClassLoader());
    }

    public static final Creator<NutritionLabel> CREATOR = new Creator<NutritionLabel>() {
        @Override
        public NutritionLabel createFromParcel(Parcel in) {
            return new NutritionLabel(in);
        }

        @Override
        public NutritionLabel[] newArray(int size) {
            return new NutritionLabel[size];
        }
    };

    public Calcium getCalcium() {
        return calcium;
    }

    public void setCalcium(Calcium calcium) {
        this.calcium = calcium;
    }

    public Carbohydrates getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Carbohydrates carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Cholesterol getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Cholesterol cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Calories getCalories() {
        return calories;
    }

    public void setCalories(Calories calories) {
        this.calories = calories;
    }

    public Fat getFat() {
        return fat;
    }

    public void setFat(Fat fat) {
        this.fat = fat;
    }

    public Fiber getFiber() {
        return fiber;
    }

    public void setFiber(Fiber fiber) {
        this.fiber = fiber;
    }

    public Iron getIron() {
        return iron;
    }

    public void setIron(Iron iron) {
        this.iron = iron;
    }

    public Proteins getProteins() {
        return proteins;
    }

    public void setProteins(Proteins proteins) {
        this.proteins = proteins;
    }

    public Salt getSalt() {
        return salt;
    }

    public void setSalt(Salt salt) {
        this.salt = salt;
    }

    public SaturatedFat getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(SaturatedFat saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public Sodium getSodium() {
        return sodium;
    }

    public void setSodium(Sodium sodium) {
        this.sodium = sodium;
    }

    public Sugars getSugars() {
        return sugars;
    }

    public void setSugars(Sugars sugars) {
        this.sugars = sugars;
    }

    public TransFat getTransFat() {
        return transFat;
    }

    public void setTransFat(TransFat transFat) {
        this.transFat = transFat;
    }

    public VitaminA getVitaminA() {
        return vitaminA;
    }

    public void setVitaminA(VitaminA vitaminA) {
        this.vitaminA = vitaminA;
    }

    public VitaminC getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(VitaminC vitaminC) {
        this.vitaminC = vitaminC;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(calcium, flags);
        dest.writeParcelable(carbohydrates, flags);
        dest.writeParcelable(cholesterol, flags);
        dest.writeParcelable(calories, flags);
        dest.writeParcelable(fat, flags);
        dest.writeParcelable(fiber, flags);
        dest.writeParcelable(iron, flags);
        dest.writeParcelable(proteins, flags);
        dest.writeParcelable(salt, flags);
        dest.writeParcelable(saturatedFat, flags);
        dest.writeParcelable(sodium, flags);
        dest.writeParcelable(sugars, flags);
        dest.writeParcelable(transFat, flags);
        dest.writeParcelable(vitaminA, flags);
        dest.writeParcelable(vitaminC, flags);
    }

    @Override
    public String toString() {
        return "NutritionLabel{" +
                "calcium=" + calcium +
                ", carbohydrates=" + carbohydrates +
                ", cholesterol=" + cholesterol +
                ", calories=" + calories +
                ", fat=" + fat +
                ", fiber=" + fiber +
                ", iron=" + iron +
                ", proteins=" + proteins +
                ", salt=" + salt +
                ", saturatedFat=" + saturatedFat +
                ", sodium=" + sodium +
                ", sugars=" + sugars +
                ", transFat=" + transFat +
                ", vitaminA=" + vitaminA +
                ", vitaminC=" + vitaminC +
                '}';
    }
}
