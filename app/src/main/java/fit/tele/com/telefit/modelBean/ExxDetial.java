
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExxDetial implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("u_exe_pln_id")
    @Expose
    private String uExePlnId;
    @SerializedName("exe_id")
    @Expose
    private String exeId;
    @SerializedName("exe_hours")
    @Expose
    private String exeHours;
    @SerializedName("exe_min")
    @Expose
    private String exeMin;
    @SerializedName("exe_sec")
    @Expose
    private String exeSec;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("plane_datee")
    @Expose
    private String planeDatee;
    @SerializedName("exe")
    @Expose
    private ExercisesListBean exes;
    @SerializedName("setsReps")
    @Expose
    private ArrayList<SetsRepsBean> setsReps = null;

    public ExxDetial() {
    }

    protected ExxDetial(Parcel in) {
        id = in.readString();
        userId = in.readString();
        catId = in.readString();
        uExePlnId = in.readString();
        exeId = in.readString();
        exeHours = in.readString();
        exeMin = in.readString();
        exeSec = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        planeDatee = in.readString();
        exes = in.readParcelable(ExercisesListBean.class.getClassLoader());
        setsReps = in.createTypedArrayList(SetsRepsBean.CREATOR);
    }

    public static final Creator<ExxDetial> CREATOR = new Creator<ExxDetial>() {
        @Override
        public ExxDetial createFromParcel(Parcel in) {
            return new ExxDetial(in);
        }

        @Override
        public ExxDetial[] newArray(int size) {
            return new ExxDetial[size];
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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getuExePlnId() {
        return uExePlnId;
    }

    public void setuExePlnId(String uExePlnId) {
        this.uExePlnId = uExePlnId;
    }

    public String getExeId() {
        return exeId;
    }

    public void setExeId(String exeId) {
        this.exeId = exeId;
    }

    public String getExeHours() {
        return exeHours;
    }

    public void setExeHours(String exeHours) {
        this.exeHours = exeHours;
    }

    public String getExeMin() {
        return exeMin;
    }

    public void setExeMin(String exeMin) {
        this.exeMin = exeMin;
    }

    public String getExeSec() {
        return exeSec;
    }

    public void setExeSec(String exeSec) {
        this.exeSec = exeSec;
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

    public String getPlaneDatee() {
        return planeDatee;
    }

    public void setPlaneDatee(String planeDatee) {
        this.planeDatee = planeDatee;
    }

    public ExercisesListBean getExes() {
        return exes;
    }

    public void setExes(ExercisesListBean exes) {
        this.exes = exes;
    }

    public ArrayList<SetsRepsBean> getSetsReps() {
        return setsReps;
    }

    public void setSetsReps(ArrayList<SetsRepsBean> setsReps) {
        this.setsReps = setsReps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(catId);
        dest.writeString(uExePlnId);
        dest.writeString(exeId);
        dest.writeString(exeHours);
        dest.writeString(exeMin);
        dest.writeString(exeSec);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(planeDatee);
        dest.writeParcelable(exes, flags);
        dest.writeTypedList(setsReps);
    }

    @Override
    public String toString() {
        return "ExxDetial{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", catId='" + catId + '\'' +
                ", uExePlnId='" + uExePlnId + '\'' +
                ", exeId='" + exeId + '\'' +
                ", exeHours='" + exeHours + '\'' +
                ", exeMin='" + exeMin + '\'' +
                ", exeSec='" + exeSec + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", planeDatee='" + planeDatee + '\'' +
                ", exes=" + exes +
                ", setsReps=" + setsReps +
                '}';
    }
}
