
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceBean implements Parcelable {

    @SerializedName("device_image")
    @Expose
    private int deviceImage;
    @SerializedName("device_name")
    @Expose
    private String deviceName;
    @SerializedName("last_sync")
    @Expose
    private String lastSync;

    public DeviceBean() {
    }

    protected DeviceBean(Parcel in) {
        deviceImage = in.readInt();
        deviceName = in.readString();
        lastSync = in.readString();
    }

    public static final Creator<DeviceBean> CREATOR = new Creator<DeviceBean>() {
        @Override
        public DeviceBean createFromParcel(Parcel in) {
            return new DeviceBean(in);
        }

        @Override
        public DeviceBean[] newArray(int size) {
            return new DeviceBean[size];
        }
    };

    public int getDeviceImage() {
        return deviceImage;
    }

    public void setDeviceImage(int deviceImage) {
        this.deviceImage = deviceImage;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(deviceImage);
        dest.writeString(deviceName);
        dest.writeString(lastSync);
    }

    @Override
    public String toString() {
        return "DeviceBean{" +
                "deviceImage=" + deviceImage +
                ", deviceName='" + deviceName + '\'' +
                ", lastSync='" + lastSync + '\'' +
                '}';
    }
}
