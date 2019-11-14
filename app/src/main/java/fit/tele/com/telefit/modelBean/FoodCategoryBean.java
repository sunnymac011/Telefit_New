
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoodCategoryBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("food_type")
    @Expose
    private String foodType;
    @SerializedName("is_admin")
    @Expose
    private String isAdmin;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("is_customer")
    @Expose
    private String isCustomer;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_recipe")
    @Expose
    private String isRecipe;
    @SerializedName("total_calories_week")
    @Expose
    private String totalCaloriesWeek;
    @SerializedName("recipe")
    @Expose
    private ArrayList<RecipeBean> recipeBeans;

    protected FoodCategoryBean(Parcel in) {
        id = in.readString();
        foodType = in.readString();
        isAdmin = in.readString();
        isActive = in.readString();
        isDelete = in.readString();
        isCustomer = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        isRecipe = in.readString();
        totalCaloriesWeek = in.readString();
        recipeBeans = in.createTypedArrayList(RecipeBean.CREATOR);
    }

    public static final Creator<FoodCategoryBean> CREATOR = new Creator<FoodCategoryBean>() {
        @Override
        public FoodCategoryBean createFromParcel(Parcel in) {
            return new FoodCategoryBean(in);
        }

        @Override
        public FoodCategoryBean[] newArray(int size) {
            return new FoodCategoryBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
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

    public String getIsRecipe() {
        return isRecipe;
    }

    public void setIsRecipe(String isRecipe) {
        this.isRecipe = isRecipe;
    }

    public String getTotalCaloriesWeek() {
        return totalCaloriesWeek;
    }

    public void setTotalCaloriesWeek(String totalCaloriesWeek) {
        this.totalCaloriesWeek = totalCaloriesWeek;
    }

    public ArrayList<RecipeBean> getRecipeBeans() {
        return recipeBeans;
    }

    public void setRecipeBeans(ArrayList<RecipeBean> recipeBeans) {
        this.recipeBeans = recipeBeans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(foodType);
        dest.writeString(isAdmin);
        dest.writeString(isActive);
        dest.writeString(isDelete);
        dest.writeString(isCustomer);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(isRecipe);
        dest.writeString(totalCaloriesWeek);
        dest.writeTypedList(recipeBeans);
    }

    @Override
    public String toString() {
        return "FoodCategoryBean{" +
                "id='" + id + '\'' +
                ", foodType='" + foodType + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", isActive='" + isActive + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", isCustomer='" + isCustomer + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", isRecipe='" + isRecipe + '\'' +
                ", totalCaloriesWeek='" + totalCaloriesWeek + '\'' +
                ", recipeBeans=" + recipeBeans +
                '}';
    }
}
