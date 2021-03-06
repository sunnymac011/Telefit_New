
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Front implements Parcelable {

    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("display")
    @Expose
    private String display;

    protected Front(Parcel in) {
        small = in.readString();
        thumb = in.readString();
        display = in.readString();
    }

    public static final Creator<Front> CREATOR = new Creator<Front>() {
        @Override
        public Front createFromParcel(Parcel in) {
            return new Front(in);
        }

        @Override
        public Front[] newArray(int size) {
            return new Front[size];
        }
    };

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(thumb);
        dest.writeString(display);
    }

    @Override
    public String toString() {
        return "Front{" +
                "small='" + small + '\'' +
                ", thumb='" + thumb + '\'' +
                ", display='" + display + '\'' +
                '}';
    }
}
