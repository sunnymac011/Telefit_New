
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fit.tele.com.telefit.modelBean.chompBeans.NutritionLabel;

public class PrivacyBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;

    public PrivacyBean() {
    }

    protected PrivacyBean(Parcel in) {
        id = in.readString();
        title = in.readString();
        details = in.readString();
    }

    public static final Creator<PrivacyBean> CREATOR = new Creator<PrivacyBean>() {
        @Override
        public PrivacyBean createFromParcel(Parcel in) {
            return new PrivacyBean(in);
        }

        @Override
        public PrivacyBean[] newArray(int size) {
            return new PrivacyBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(details);
    }

    @Override
    public String toString() {
        return "PrivacyBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
