package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoutinePrefBean implements Parcelable {

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

    public RoutinePrefBean() {
    }

    protected RoutinePrefBean(Parcel in) {
        routineName = in.readString();
        dayOfTheWeek = in.readString();
        routineType = in.readString();
        difficultyLevel = in.readString();
        dayFlag = in.readString();
    }

    public static final Creator<RoutinePrefBean> CREATOR = new Creator<RoutinePrefBean>() {
        @Override
        public RoutinePrefBean createFromParcel(Parcel in) {
            return new RoutinePrefBean(in);
        }

        @Override
        public RoutinePrefBean[] newArray(int size) {
            return new RoutinePrefBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(routineName);
        dest.writeString(dayOfTheWeek);
        dest.writeString(routineType);
        dest.writeString(difficultyLevel);
        dest.writeString(dayFlag);
    }

    @Override
    public String toString() {
        return "RoutinePrefBean{" +
                "routineName='" + routineName + '\'' +
                ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                ", routineType='" + routineType + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", dayFlag='" + dayFlag + '\'' +
                '}';
    }
}
