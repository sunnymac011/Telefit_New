
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoutinePlanBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("routine_name")
    @Expose
    private String routineName;
    @SerializedName("day_of_the_week")
    @Expose
    private String dayOfTheWeek;
    @SerializedName("routine_type")
    @Expose
    private String routineType;
    @SerializedName("difficulty_level")
    @Expose
    private String difficultyLevel;
    @SerializedName("is_user_trainer")
    @Expose
    private String isUserTrainer;
    @SerializedName("day_flag")
    @Expose
    private String dayFlag;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("total_exe")
    @Expose
    private String totalExe;
    @SerializedName("plane_date")
    @Expose
    private String planeDate;

    protected RoutinePlanBean(Parcel in) {
        id = in.readString();
        userId = in.readString();
        routineName = in.readString();
        dayOfTheWeek = in.readString();
        routineType = in.readString();
        difficultyLevel = in.readString();
        isUserTrainer = in.readString();
        dayFlag = in.readString();
        isDelete = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        totalExe = in.readString();
        planeDate = in.readString();
    }

    public static final Creator<RoutinePlanBean> CREATOR = new Creator<RoutinePlanBean>() {
        @Override
        public RoutinePlanBean createFromParcel(Parcel in) {
            return new RoutinePlanBean(in);
        }

        @Override
        public RoutinePlanBean[] newArray(int size) {
            return new RoutinePlanBean[size];
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

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getIsUserTrainer() {
        return isUserTrainer;
    }

    public void setIsUserTrainer(String isUserTrainer) {
        this.isUserTrainer = isUserTrainer;
    }

    public String getDayFlag() {
        return dayFlag;
    }

    public void setDayFlag(String dayFlag) {
        this.dayFlag = dayFlag;
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

    public String getTotalExe() {
        return totalExe;
    }

    public void setTotalExe(String totalExe) {
        this.totalExe = totalExe;
    }

    public String getPlaneDate() {
        return planeDate;
    }

    public void setPlaneDate(String planeDate) {
        this.planeDate = planeDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(routineName);
        dest.writeString(dayOfTheWeek);
        dest.writeString(routineType);
        dest.writeString(difficultyLevel);
        dest.writeString(isUserTrainer);
        dest.writeString(dayFlag);
        dest.writeString(isDelete);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(totalExe);
        dest.writeString(planeDate);
    }

    @Override
    public String toString() {
        return "RoutinePlanBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", routineName='" + routineName + '\'' +
                ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                ", routineType='" + routineType + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", isUserTrainer='" + isUserTrainer + '\'' +
                ", dayFlag='" + dayFlag + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", totalExe='" + totalExe + '\'' +
                ", planeDate='" + planeDate + '\'' +
                '}';
    }
}
