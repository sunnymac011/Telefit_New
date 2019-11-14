
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryBean implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("iso")
    @Expose
    public String iso;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("nicename")
    @Expose
    public String nicename;
    @SerializedName("iso3")
    @Expose
    public String iso3;
    @SerializedName("numcode")
    @Expose
    public String numcode;
    @SerializedName("phonecode")
    @Expose
    public String phonecode;
    @SerializedName("default_select")
    @Expose
    public String defaultSelect;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    protected CountryBean(Parcel in) {
        id = in.readString();
        iso = in.readString();
        name = in.readString();
        nicename = in.readString();
        iso3 = in.readString();
        numcode = in.readString();
        phonecode = in.readString();
        defaultSelect = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<CountryBean> CREATOR = new Creator<CountryBean>() {
        @Override
        public CountryBean createFromParcel(Parcel in) {
            return new CountryBean(in);
        }

        @Override
        public CountryBean[] newArray(int size) {
            return new CountryBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getDefaultSelect() {
        return defaultSelect;
    }

    public void setDefaultSelect(String defaultSelect) {
        this.defaultSelect = defaultSelect;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso);
        dest.writeString(name);
        dest.writeString(nicename);
        dest.writeString(iso3);
        dest.writeString(numcode);
        dest.writeString(phonecode);
        dest.writeString(defaultSelect);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public String toString() {
        return "CountryBean{" +
                "id='" + id + '\'' +
                ", iso='" + iso + '\'' +
                ", name='" + name + '\'' +
                ", nicename='" + nicename + '\'' +
                ", iso3='" + iso3 + '\'' +
                ", numcode='" + numcode + '\'' +
                ", phonecode='" + phonecode + '\'' +
                ", defaultSelect='" + defaultSelect + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
