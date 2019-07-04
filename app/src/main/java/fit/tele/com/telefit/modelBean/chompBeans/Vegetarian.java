
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vegetarian implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_compatible")
    @Expose
    private String isCompatible;
    @SerializedName("compatibility")
    @Expose
    private String compatibility;
    @SerializedName("grade_confidence")
    @Expose
    private String gradeConfidence;
    @SerializedName("grade_confidence_desc")
    @Expose
    private String gradeConfidenceDesc;

    protected Vegetarian(Parcel in) {
        id = in.readString();
        name = in.readString();
        isCompatible = in.readString();
        compatibility = in.readString();
        gradeConfidence = in.readString();
        gradeConfidenceDesc = in.readString();
    }

    public static final Creator<Vegetarian> CREATOR = new Creator<Vegetarian>() {
        @Override
        public Vegetarian createFromParcel(Parcel in) {
            return new Vegetarian(in);
        }

        @Override
        public Vegetarian[] newArray(int size) {
            return new Vegetarian[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCompatible() {
        return isCompatible;
    }

    public void setIsCompatible(String isCompatible) {
        this.isCompatible = isCompatible;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getGradeConfidence() {
        return gradeConfidence;
    }

    public void setGradeConfidence(String gradeConfidence) {
        this.gradeConfidence = gradeConfidence;
    }

    public String getGradeConfidenceDesc() {
        return gradeConfidenceDesc;
    }

    public void setGradeConfidenceDesc(String gradeConfidenceDesc) {
        this.gradeConfidenceDesc = gradeConfidenceDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(isCompatible);
        dest.writeString(compatibility);
        dest.writeString(gradeConfidence);
        dest.writeString(gradeConfidenceDesc);
    }

    @Override
    public String toString() {
        return "Vegetarian{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isCompatible='" + isCompatible + '\'' +
                ", compatibility='" + compatibility + '\'' +
                ", gradeConfidence='" + gradeConfidence + '\'' +
                ", gradeConfidenceDesc='" + gradeConfidenceDesc + '\'' +
                '}';
    }
}
