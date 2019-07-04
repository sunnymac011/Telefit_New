
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryDetails implements Parcelable {

    @SerializedName("english_speaking")
    @Expose
    private String englishSpeaking;
    @SerializedName("non_english_speaking")
    @Expose
    private String nonEnglishSpeaking;

    protected CountryDetails(Parcel in) {
        englishSpeaking = in.readString();
        nonEnglishSpeaking = in.readString();
    }

    public static final Creator<CountryDetails> CREATOR = new Creator<CountryDetails>() {
        @Override
        public CountryDetails createFromParcel(Parcel in) {
            return new CountryDetails(in);
        }

        @Override
        public CountryDetails[] newArray(int size) {
            return new CountryDetails[size];
        }
    };

    public String getEnglishSpeaking() {
        return englishSpeaking;
    }

    public void setEnglishSpeaking(String englishSpeaking) {
        this.englishSpeaking = englishSpeaking;
    }

    public String getNonEnglishSpeaking() {
        return nonEnglishSpeaking;
    }

    public void setNonEnglishSpeaking(String nonEnglishSpeaking) {
        this.nonEnglishSpeaking = nonEnglishSpeaking;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(englishSpeaking);
        dest.writeString(nonEnglishSpeaking);
    }

    @Override
    public String toString() {
        return "CountryDetails{" +
                "englishSpeaking='" + englishSpeaking + '\'' +
                ", nonEnglishSpeaking='" + nonEnglishSpeaking + '\'' +
                '}';
    }
}
