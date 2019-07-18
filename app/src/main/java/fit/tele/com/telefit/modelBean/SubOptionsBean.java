
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubOptionsBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("sub_cat_option")
    @Expose
    private String subCatOption;
    @SerializedName("sub_cat_detail")
    @Expose
    private String subCatDetail;
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
    @SerializedName("isCheck")
    @Expose
    private boolean isCheck;
    @SerializedName("sub_cat_image_url")
    @Expose
    private String subCatImageUrl;

    protected SubOptionsBean(Parcel in) {
        id = in.readString();
        catId = in.readString();
        subCatId = in.readString();
        subCatOption = in.readString();
        subCatDetail = in.readString();
        isActive = in.readString();
        isDelete = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        isCheck = in.readByte() != 0;
        subCatImageUrl = in.readString();
    }

    public static final Creator<SubOptionsBean> CREATOR = new Creator<SubOptionsBean>() {
        @Override
        public SubOptionsBean createFromParcel(Parcel in) {
            return new SubOptionsBean(in);
        }

        @Override
        public SubOptionsBean[] newArray(int size) {
            return new SubOptionsBean[size];
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

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatOption() {
        return subCatOption;
    }

    public void setSubCatOption(String subCatOption) {
        this.subCatOption = subCatOption;
    }

    public String getSubCatDetail() {
        return subCatDetail;
    }

    public void setSubCatDetail(String subCatDetail) {
        this.subCatDetail = subCatDetail;
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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getSubCatImageUrl() {
        return subCatImageUrl;
    }

    public void setSubCatImageUrl(String subCatImageUrl) {
        this.subCatImageUrl = subCatImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(catId);
        dest.writeString(subCatId);
        dest.writeString(subCatOption);
        dest.writeString(subCatDetail);
        dest.writeString(isActive);
        dest.writeString(isDelete);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeString(subCatImageUrl);
    }
}
