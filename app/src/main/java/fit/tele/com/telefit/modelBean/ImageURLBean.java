
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageURLBean implements Parcelable {

    @SerializedName("img")
    @Expose
    private String imgUrl;

    public ImageURLBean() {
    }

    protected ImageURLBean(Parcel in) {
        imgUrl = in.readString();
    }

    public static final Creator<ImageURLBean> CREATOR = new Creator<ImageURLBean>() {
        @Override
        public ImageURLBean createFromParcel(Parcel in) {
            return new ImageURLBean(in);
        }

        @Override
        public ImageURLBean[] newArray(int size) {
            return new ImageURLBean[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
    }

    @Override
    public String toString() {
        return "ImageURLBean{" +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
