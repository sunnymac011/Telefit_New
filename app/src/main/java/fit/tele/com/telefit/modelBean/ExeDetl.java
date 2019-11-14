
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExeDetl implements Parcelable {

    @SerializedName("exeid")
    @Expose
    private String exeid;
    @SerializedName("cat_id")
    @Expose
    private String catid;
    @SerializedName("exe_hours")
    @Expose
    private String exeHours;
    @SerializedName("exe_min")
    @Expose
    private String exeMin;
    @SerializedName("exe_sec")
    @Expose
    private String exeSec;
    @SerializedName("routine_exe_order")
    @Expose
    private String routineExeOrder;
    @SerializedName("setsReps")
    @Expose
    private ArrayList<SetsRepsBean> setsRepsBeans;

    public ExeDetl() {
    }

    protected ExeDetl(Parcel in) {
        exeid = in.readString();
        catid = in.readString();
        exeHours = in.readString();
        exeMin = in.readString();
        exeSec = in.readString();
        routineExeOrder = in.readString();
        setsRepsBeans = in.createTypedArrayList(SetsRepsBean.CREATOR);
    }

    public static final Creator<ExeDetl> CREATOR = new Creator<ExeDetl>() {
        @Override
        public ExeDetl createFromParcel(Parcel in) {
            return new ExeDetl(in);
        }

        @Override
        public ExeDetl[] newArray(int size) {
            return new ExeDetl[size];
        }
    };

    public String getExeid() {
        return exeid;
    }

    public void setExeid(String exeid) {
        this.exeid = exeid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
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

    public String getRoutineExeOrder() {
        return routineExeOrder;
    }

    public void setRoutineExeOrder(String routineExeOrder) {
        this.routineExeOrder = routineExeOrder;
    }

    public ArrayList<SetsRepsBean> getSetsRepsBeans() {
        return setsRepsBeans;
    }

    public void setSetsRepsBeans(ArrayList<SetsRepsBean> setsRepsBeans) {
        this.setsRepsBeans = setsRepsBeans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exeid);
        dest.writeString(catid);
        dest.writeString(exeHours);
        dest.writeString(exeMin);
        dest.writeString(exeSec);
        dest.writeString(routineExeOrder);
        dest.writeTypedList(setsRepsBeans);
    }

    @Override
    public String toString() {
        return "ExeDetl{" +
                "exeid='" + exeid + '\'' +
                ", catid='" + catid + '\'' +
                ", exeHours='" + exeHours + '\'' +
                ", exeMin='" + exeMin + '\'' +
                ", exeSec='" + exeSec + '\'' +
                ", routineExeOrder='" + routineExeOrder + '\'' +
                ", setsRepsBeans=" + setsRepsBeans +
                '}';
    }
}
