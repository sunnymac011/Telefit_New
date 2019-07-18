package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoutinePlanListBean implements Parcelable {

    @SerializedName("future")
    @Expose
    private ArrayList<RoutinePlanBean> future = null;
    @SerializedName("past")
    @Expose
    private ArrayList<RoutinePlanBean> past = null;

    protected RoutinePlanListBean(Parcel in) {
        future = in.createTypedArrayList(RoutinePlanBean.CREATOR);
        past = in.createTypedArrayList(RoutinePlanBean.CREATOR);
    }

    public static final Creator<RoutinePlanListBean> CREATOR = new Creator<RoutinePlanListBean>() {
        @Override
        public RoutinePlanListBean createFromParcel(Parcel in) {
            return new RoutinePlanListBean(in);
        }

        @Override
        public RoutinePlanListBean[] newArray(int size) {
            return new RoutinePlanListBean[size];
        }
    };

    public ArrayList<RoutinePlanBean> getFuture() {
        return future;
    }

    public void setFuture(ArrayList<RoutinePlanBean> future) {
        this.future = future;
    }

    public ArrayList<RoutinePlanBean> getPast() {
        return past;
    }

    public void setPast(ArrayList<RoutinePlanBean> past) {
        this.past = past;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(future);
        dest.writeTypedList(past);
    }

    @Override
    public String toString() {
        return "RoutinePlanListBean{" +
                "future=" + future +
                ", past=" + past +
                '}';
    }
}
