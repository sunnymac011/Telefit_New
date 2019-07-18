
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubExerciseBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_name")
    @Expose
    private String subCatName;
    @SerializedName("sub_cat_detail")
    @Expose
    private String subCatDetail;
    @SerializedName("sub_cat_image")
    @Expose
    private String subCatImage;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("sub_cat_image_url")
    @Expose
    private String subCatImageUrl;
    @SerializedName("isCheck")
    @Expose
    private boolean isCheck = false;
    @SerializedName("api_subcatopt_exe")
    @Expose
    private ArrayList<SubOptionsBean> apiSubcatoptExe = null;

    protected SubExerciseBean(Parcel in) {
        id = in.readString();
        catId = in.readString();
        subCatName = in.readString();
        subCatDetail = in.readString();
        subCatImage = in.readString();
        isActive = in.readString();
        isDelete = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        subCatImageUrl = in.readString();
        isCheck = in.readByte() != 0;
        apiSubcatoptExe = in.createTypedArrayList(SubOptionsBean.CREATOR);
    }

    public static final Creator<SubExerciseBean> CREATOR = new Creator<SubExerciseBean>() {
        @Override
        public SubExerciseBean createFromParcel(Parcel in) {
            return new SubExerciseBean(in);
        }

        @Override
        public SubExerciseBean[] newArray(int size) {
            return new SubExerciseBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatDetail() {
        return subCatDetail;
    }

    public void setSubCatDetail(String subCatDetail) {
        this.subCatDetail = subCatDetail;
    }

    public String getSubCatImage() {
        return subCatImage;
    }

    public void setSubCatImage(String subCatImage) {
        this.subCatImage = subCatImage;
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

    public String getSubCatImageUrl() {
        return subCatImageUrl;
    }

    public void setSubCatImageUrl(String subCatImageUrl) {
        this.subCatImageUrl = subCatImageUrl;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public ArrayList<SubOptionsBean> getApiSubcatoptExe() {
        return apiSubcatoptExe;
    }

    public void setApiSubcatoptExe(ArrayList<SubOptionsBean> apiSubcatoptExe) {
        this.apiSubcatoptExe = apiSubcatoptExe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(catId);
        dest.writeString(subCatName);
        dest.writeString(subCatDetail);
        dest.writeString(subCatImage);
        dest.writeString(isActive);
        dest.writeString(isDelete);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(subCatImageUrl);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeTypedList(apiSubcatoptExe);
    }

    @Override
    public String toString() {
        return "SubExerciseBean{" +
                "id='" + id + '\'' +
                ", catId='" + catId + '\'' +
                ", subCatName='" + subCatName + '\'' +
                ", subCatDetail='" + subCatDetail + '\'' +
                ", subCatImage='" + subCatImage + '\'' +
                ", isActive='" + isActive + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", subCatImageUrl='" + subCatImageUrl + '\'' +
                ", isCheck=" + isCheck +
                ", apiSubcatoptExe=" + apiSubcatoptExe +
                '}';
    }
}
