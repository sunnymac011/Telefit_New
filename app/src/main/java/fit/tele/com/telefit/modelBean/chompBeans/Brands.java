
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brands implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("recipe_id")
    @Expose
    private String recipeId;
    @SerializedName("food_id")
    @Expose
    private String foodId;
    @SerializedName("gardein")
    @Expose
    private String gardein;
    @SerializedName("garden_protein_international_inc")
    @Expose
    private String gardenProteinInternationalInc;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    protected Brands(Parcel in) {
        id = in.readString();
        recipeId = in.readString();
        foodId = in.readString();
        gardein = in.readString();
        gardenProteinInternationalInc = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Brands> CREATOR = new Creator<Brands>() {
        @Override
        public Brands createFromParcel(Parcel in) {
            return new Brands(in);
        }

        @Override
        public Brands[] newArray(int size) {
            return new Brands[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getGardein() {
        return gardein;
    }

    public void setGardein(String gardein) {
        this.gardein = gardein;
    }

    public String getGardenProteinInternationalInc() {
        return gardenProteinInternationalInc;
    }

    public void setGardenProteinInternationalInc(String gardenProteinInternationalInc) {
        this.gardenProteinInternationalInc = gardenProteinInternationalInc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(recipeId);
        dest.writeString(foodId);
        dest.writeString(gardein);
        dest.writeString(gardenProteinInternationalInc);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public String toString() {
        return "Brands{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", foodId='" + foodId + '\'' +
                ", gardein='" + gardein + '\'' +
                ", gardenProteinInternationalInc='" + gardenProteinInternationalInc + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
