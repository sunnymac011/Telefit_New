
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdatePlanApiBean implements Parcelable {

    @SerializedName("exe_id")
    @Expose
    private ArrayList<ExeDetl> exeId = null;
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
    @SerializedName("day_flag")
    @Expose
    private String dayFlag;
    @SerializedName("routine_p_id")
    @Expose
    private String routinePId;

    public UpdatePlanApiBean() {
    }

    protected UpdatePlanApiBean(Parcel in) {
        exeId = in.createTypedArrayList(ExeDetl.CREATOR);
        routineName = in.readString();
        dayOfTheWeek = in.readString();
        routineType = in.readString();
        difficultyLevel = in.readString();
        dayFlag = in.readString();
        routinePId = in.readString();
    }

    public static final Creator<UpdatePlanApiBean> CREATOR = new Creator<UpdatePlanApiBean>() {
        @Override
        public UpdatePlanApiBean createFromParcel(Parcel in) {
            return new UpdatePlanApiBean(in);
        }

        @Override
        public UpdatePlanApiBean[] newArray(int size) {
            return new UpdatePlanApiBean[size];
        }
    };

    public ArrayList<ExeDetl> getExeId() {
        return exeId;
    }

    public void setExeId(ArrayList<ExeDetl> exeId) {
        this.exeId = exeId;
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

    public String getDayFlag() {
        return dayFlag;
    }

    public void setDayFlag(String dayFlag) {
        this.dayFlag = dayFlag;
    }

    public String getRoutinePId() {
        return routinePId;
    }

    public void setRoutinePId(String routinePId) {
        this.routinePId = routinePId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(exeId);
        dest.writeString(routineName);
        dest.writeString(dayOfTheWeek);
        dest.writeString(routineType);
        dest.writeString(difficultyLevel);
        dest.writeString(dayFlag);
        dest.writeString(routinePId);
    }

    @Override
    public String toString() {
        return "UpdatePlanApiBean{" +
                "exeId=" + exeId +
                ", routineName='" + routineName + '\'' +
                ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                ", routineType='" + routineType + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", dayFlag='" + dayFlag + '\'' +
                ", routinePId='" + routinePId + '\'' +
                '}';
    }
}
