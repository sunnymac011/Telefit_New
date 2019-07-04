
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brands implements Parcelable {

    @SerializedName("gardein")
    @Expose
    private String gardein;
    @SerializedName("garden-protein-international-inc")
    @Expose
    private String gardenProteinInternationalInc;

    protected Brands(Parcel in) {
        gardein = in.readString();
        gardenProteinInternationalInc = in.readString();
    }

    public static final Creator<Brands> CREATOR = new Creator<Brands>() {
        @Override
        public Brands createFromParcel(Parcel in) {
            return new Brands(in);
        }

        @Override
        public Brands[] newArray(int size) {
            return new Brands[size];
        }
    };

    public String getGardein() {
        return gardein;
    }

    public void setGardein(String gardein) {
        this.gardein = gardein;
    }

    public String getGardenProteinInternationalInc() {
        return gardenProteinInternationalInc;
    }

    public void setGardenProteinInternationalInc(String gardenProteinInternationalInc) {
        this.gardenProteinInternationalInc = gardenProteinInternationalInc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gardein);
        dest.writeString(gardenProteinInternationalInc);
    }

    @Override
    public String toString() {
        return "Brands{" +
                "gardein='" + gardein + '\'' +
                ", gardenProteinInternationalInc='" + gardenProteinInternationalInc + '\'' +
                '}';
    }
}
