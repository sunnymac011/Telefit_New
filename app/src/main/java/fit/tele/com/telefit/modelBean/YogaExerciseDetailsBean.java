
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YogaExerciseDetailsBean implements Parcelable {

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
    private String exeInstructions;
    @SerializedName("is_import")
    @Expose
    private String isImport;
    @SerializedName("impot_file_name")
    @Expose
    private String impotFileName;
    @SerializedName("exe_multivideo_coverimg")
    @Expose
    private String exeMultivideoCoverimg;
    @SerializedName("deactive_date")
    @Expose
    private String deactiveDate;
    @SerializedName("is_video_deactive")
    @Expose
    private String isVideoDeactive;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("opt_array")
    @Expose
    private ArrayList<SubArray> optArray = null;
    @SerializedName("video_array")
    @Expose
    private ArrayList<VideoArrayBean> videoArray = null;

    protected YogaExerciseDetailsBean(Parcel in) {
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
        exeInstructions = in.readString();
        isImport = in.readString();
        impotFileName = in.readString();
        exeMultivideoCoverimg = in.readString();
        deactiveDate = in.readString();
        isVideoDeactive = in.readString();
        catName = in.readString();
        optArray = in.createTypedArrayList(SubArray.CREATOR);
        videoArray = in.createTypedArrayList(VideoArrayBean.CREATOR);
    }

    public static final Creator<YogaExerciseDetailsBean> CREATOR = new Creator<YogaExerciseDetailsBean>() {
        @Override
        public YogaExerciseDetailsBean createFromParcel(Parcel in) {
            return new YogaExerciseDetailsBean(in);
        }

        @Override
        public YogaExerciseDetailsBean[] newArray(int size) {
            return new YogaExerciseDetailsBean[size];
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

    public String getExeInstructions() {
        return exeInstructions;
    }

    public void setExeInstructions(String exeInstructions) {
        this.exeInstructions = exeInstructions;
    }

    public String getIsImport() {
        return isImport;
    }

    public void setIsImport(String isImport) {
        this.isImport = isImport;
    }

    public String getImpotFileName() {
        return impotFileName;
    }

    public void setImpotFileName(String impotFileName) {
        this.impotFileName = impotFileName;
    }

    public String getExeMultivideoCoverimg() {
        return exeMultivideoCoverimg;
    }

    public void setExeMultivideoCoverimg(String exeMultivideoCoverimg) {
        this.exeMultivideoCoverimg = exeMultivideoCoverimg;
    }

    public String getDeactiveDate() {
        return deactiveDate;
    }

    public void setDeactiveDate(String deactiveDate) {
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

    public ArrayList<SubArray> getOptArray() {
        return optArray;
    }

    public void setOptArray(ArrayList<SubArray> optArray) {
        this.optArray = optArray;
    }

    public ArrayList<VideoArrayBean> getVideoArray() {
        return videoArray;
    }

    public void setVideoArray(ArrayList<VideoArrayBean> videoArray) {
        this.videoArray = videoArray;
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
        dest.writeString(exeInstructions);
        dest.writeString(isImport);
        dest.writeString(impotFileName);
        dest.writeString(exeMultivideoCoverimg);
        dest.writeString(deactiveDate);
        dest.writeString(isVideoDeactive);
        dest.writeString(catName);
        dest.writeTypedList(optArray);
        dest.writeTypedList(videoArray);
    }

    @Override
    public String toString() {
        return "YogaExerciseDetailsBean{" +
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
                ", exeInstructions='" + exeInstructions + '\'' +
                ", isImport='" + isImport + '\'' +
                ", impotFileName='" + impotFileName + '\'' +
                ", exeMultivideoCoverimg='" + exeMultivideoCoverimg + '\'' +
                ", deactiveDate='" + deactiveDate + '\'' +
                ", isVideoDeactive='" + isVideoDeactive + '\'' +
                ", catName='" + catName + '\'' +
                ", optArray=" + optArray +
                ", videoArray=" + videoArray +
                '}';
    }
}
