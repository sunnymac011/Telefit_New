
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoutineExerciseBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("exe_title")
    @Expose
    private String exeTitle;
    @SerializedName("exe_desc")
    @Expose
    private String exeDesc;
    @SerializedName("exe_video")
    @Expose
    private String exeVideo;
    @SerializedName("exe_video_cover_img")
    @Expose
    private String exeVideoCoverImg;
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
    @SerializedName("exe_instructions")
    @Expose
    private Object exeInstructions;
    @SerializedName("is_import")
    @Expose
    private String isImport;
    @SerializedName("impot_file_name")
    @Expose
    private Object impotFileName;
    @SerializedName("exe_multivideo_coverimg")
    @Expose
    private Object exeMultivideoCoverimg;
    @SerializedName("deactive_date")
    @Expose
    private Object deactiveDate;
    @SerializedName("is_video_deactive")
    @Expose
    private String isVideoDeactive;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("exe_image_url")
    @Expose
    private String exeImageUrl;
    @SerializedName("exe_video_url")
    @Expose
    private String exeVideoUrl;
    @SerializedName("opt_array")
    @Expose
    private OptArray optArray;

    protected RoutineExerciseBean(Parcel in) {
        id = in.readString();
        catId = in.readString();
        exeTitle = in.readString();
        exeDesc = in.readString();
        exeVideo = in.readString();
        exeVideoCoverImg = in.readString();
        isActive = in.readString();
        isDelete = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        isImport = in.readString();
        isVideoDeactive = in.readString();
        catName = in.readString();
        exeImageUrl = in.readString();
        exeVideoUrl = in.readString();
        optArray = in.readParcelable(OptArray.class.getClassLoader());
    }

    public static final Creator<RoutineExerciseBean> CREATOR = new Creator<RoutineExerciseBean>() {
        @Override
        public RoutineExerciseBean createFromParcel(Parcel in) {
            return new RoutineExerciseBean(in);
        }

        @Override
        public RoutineExerciseBean[] newArray(int size) {
            return new RoutineExerciseBean[size];
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

    public String getExeTitle() {
        return exeTitle;
    }

    public void setExeTitle(String exeTitle) {
        this.exeTitle = exeTitle;
    }

    public String getExeDesc() {
        return exeDesc;
    }

    public void setExeDesc(String exeDesc) {
        this.exeDesc = exeDesc;
    }

    public String getExeVideo() {
        return exeVideo;
    }

    public void setExeVideo(String exeVideo) {
        this.exeVideo = exeVideo;
    }

    public String getExeVideoCoverImg() {
        return exeVideoCoverImg;
    }

    public void setExeVideoCoverImg(String exeVideoCoverImg) {
        this.exeVideoCoverImg = exeVideoCoverImg;
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

    public Object getExeInstructions() {
        return exeInstructions;
    }

    public void setExeInstructions(Object exeInstructions) {
        this.exeInstructions = exeInstructions;
    }

    public String getIsImport() {
        return isImport;
    }

    public void setIsImport(String isImport) {
        this.isImport = isImport;
    }

    public Object getImpotFileName() {
        return impotFileName;
    }

    public void setImpotFileName(Object impotFileName) {
        this.impotFileName = impotFileName;
    }

    public Object getExeMultivideoCoverimg() {
        return exeMultivideoCoverimg;
    }

    public void setExeMultivideoCoverimg(Object exeMultivideoCoverimg) {
        this.exeMultivideoCoverimg = exeMultivideoCoverimg;
    }

    public Object getDeactiveDate() {
        return deactiveDate;
    }

    public void setDeactiveDate(Object deactiveDate) {
        this.deactiveDate = deactiveDate;
    }

    public String getIsVideoDeactive() {
        return isVideoDeactive;
    }

    public void setIsVideoDeactive(String isVideoDeactive) {
        this.isVideoDeactive = isVideoDeactive;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getExeImageUrl() {
        return exeImageUrl;
    }

    public void setExeImageUrl(String exeImageUrl) {
        this.exeImageUrl = exeImageUrl;
    }

    public String getExeVideoUrl() {
        return exeVideoUrl;
    }

    public void setExeVideoUrl(String exeVideoUrl) {
        this.exeVideoUrl = exeVideoUrl;
    }

    public OptArray getOptArray() {
        return optArray;
    }

    public void setOptArray(OptArray optArray) {
        this.optArray = optArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(catId);
        dest.writeString(exeTitle);
        dest.writeString(exeDesc);
        dest.writeString(exeVideo);
        dest.writeString(exeVideoCoverImg);
        dest.writeString(isActive);
        dest.writeString(isDelete);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(isImport);
        dest.writeString(isVideoDeactive);
        dest.writeString(catName);
        dest.writeString(exeImageUrl);
        dest.writeString(exeVideoUrl);
        dest.writeParcelable(optArray, flags);
    }

    @Override
    public String toString() {
        return "RoutineExerciseBean{" +
                "id='" + id + '\'' +
                ", catId='" + catId + '\'' +
                ", exeTitle='" + exeTitle + '\'' +
                ", exeDesc='" + exeDesc + '\'' +
                ", exeVideo='" + exeVideo + '\'' +
                ", exeVideoCoverImg='" + exeVideoCoverImg + '\'' +
                ", isActive='" + isActive + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", exeInstructions=" + exeInstructions +
                ", isImport='" + isImport + '\'' +
                ", impotFileName=" + impotFileName +
                ", exeMultivideoCoverimg=" + exeMultivideoCoverimg +
                ", deactiveDate=" + deactiveDate +
                ", isVideoDeactive='" + isVideoDeactive + '\'' +
                ", catName='" + catName + '\'' +
                ", exeImageUrl='" + exeImageUrl + '\'' +
                ", exeVideoUrl='" + exeVideoUrl + '\'' +
                ", optArray=" + optArray +
                '}';
    }
}
