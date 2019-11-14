
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedRecipeListBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("is_customer")
    @Expose
    private String isCustomer;
    @SerializedName("is_racipe_meal")
    @Expose
    private String isRacipeMeal;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("total_calories")
    @Expose
    private String totalCalories;

    protected CreatedRecipeListBean(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        isDelete = in.readString();
        isCustomer = in.readString();
        isRacipeMeal = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        totalCalories = in.readString();
    }

    public static final Creator<CreatedRecipeListBean> CREATOR = new Creator<CreatedRecipeListBean>() {
        @Override
        public CreatedRecipeListBean createFromParcel(Parcel in) {
            return new CreatedRecipeListBean(in);
        }

        @Override
        public CreatedRecipeListBean[] newArray(int size) {
            return new CreatedRecipeListBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsRacipeMeal() {
        return isRacipeMeal;
    }

    public void setIsRacipeMeal(String isRacipeMeal) {
        this.isRacipeMeal = isRacipeMeal;
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

    public String getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(String totalCalories) {
        this.totalCalories = totalCalories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(isDelete);
        dest.writeString(isCustomer);
        dest.writeString(isRacipeMeal);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(totalCalories);
    }

    @Override
    public String toString() {
        return "CreatedRecipeListBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", isCustomer='" + isCustomer + '\'' +
                ", isRacipeMeal='" + isRacipeMeal + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", totalCalories='" + totalCalories + '\'' +
                '}';
    }
}
