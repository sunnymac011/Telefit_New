
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoodCategorySearchBean implements Parcelable {

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
    @SerializedName("recipe")
    @Expose
    private ArrayList<RecipeBean> recipeBeans;

    protected FoodCategorySearchBean(Parcel in) {
        id = in.readString();
        foodType = in.readString();
        isAdmin = in.readString();
        isActive = in.readString();
        isDelete = in.readString();
        isCustomer = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        isRecipe = in.readString();
        recipeBeans = in.createTypedArrayList(RecipeBean.CREATOR);
    }

    public static final Creator<FoodCategorySearchBean> CREATOR = new Creator<FoodCategorySearchBean>() {
        @Override
        public FoodCategorySearchBean createFromParcel(Parcel in) {
            return new FoodCategorySearchBean(in);
        }

        @Override
        public FoodCategorySearchBean[] newArray(int size) {
            return new FoodCategorySearchBean[size];
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
        dest.writeTypedList(recipeBeans);
    }
}
