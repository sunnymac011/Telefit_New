
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoURLBean implements Parcelable {

    @SerializedName("img")
    @Expose
    private String imgUrl;
    @SerializedName("vid")
    @Expose
    private String vidUrl;

    public VideoURLBean() {
    }

    protected VideoURLBean(Parcel in) {
        imgUrl = in.readString();
        vidUrl = in.readString();
    }

    public static final Creator<VideoURLBean> CREATOR = new Creator<VideoURLBean>() {
        @Override
        public VideoURLBean createFromParcel(Parcel in) {
            return new VideoURLBean(in);
        }

        @Override
        public VideoURLBean[] newArray(int size) {
            return new VideoURLBean[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVidUrl() {
        return vidUrl;
    }

    public void setVidUrl(String vidUrl) {
        this.vidUrl = vidUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(vidUrl);
    }

    @Override
    public String toString() {
        return "VideoURLBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", vidUrl='" + vidUrl + '\'' +
                '}';
    }
}
