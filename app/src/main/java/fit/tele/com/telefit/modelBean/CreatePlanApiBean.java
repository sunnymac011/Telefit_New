
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePlanApiBean implements Parcelable {

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
    @SerializedName("plane_date")
    @Expose
    private String routineDate;

    public CreatePlanApiBean() {
    }

    protected CreatePlanApiBean(Parcel in) {
        exeId = in.createTypedArrayList(ExeDetl.CREATOR);
        routineName = in.readString();
        dayOfTheWeek = in.readString();
        routineType = in.readString();
        difficultyLevel = in.readString();
        dayFlag = in.readString();
        routineDate = in.readString();
    }

    public static final Creator<CreatePlanApiBean> CREATOR = new Creator<CreatePlanApiBean>() {
        @Override
        public CreatePlanApiBean createFromParcel(Parcel in) {
            return new CreatePlanApiBean(in);
        }

        @Override
        public CreatePlanApiBean[] newArray(int size) {
            return new CreatePlanApiBean[size];
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

    public String getRoutineDate() {
        return routineDate;
    }

    public void setRoutineDate(String routineDate) {
        this.routineDate = routineDate;
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
        dest.writeString(routineDate);
    }

    @Override
    public String toString() {
        return "CreatePlanApiBean{" +
                "exeId=" + exeId +
                ", routineName='" + routineName + '\'' +
                ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                ", routineType='" + routineType + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", dayFlag='" + dayFlag + '\'' +
                ", routineDate='" + routineDate + '\'' +
                '}';
    }
}
