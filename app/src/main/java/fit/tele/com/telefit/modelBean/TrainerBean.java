
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainerBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("description")
    @Expose
    private String description;

    protected TrainerBean(Parcel in) {
        id = in.readString();
        name = in.readString();
        lName = in.readString();
        profilePic = in.readString();
        city = in.readString();
        state = in.readString();
        companyName = in.readString();
        gender = in.readString();
        description = in.readString();
    }

    public static final Creator<TrainerBean> CREATOR = new Creator<TrainerBean>() {
        @Override
        public TrainerBean createFromParcel(Parcel in) {
            return new TrainerBean(in);
        }

        @Override
        public TrainerBean[] newArray(int size) {
            return new TrainerBean[size];
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

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(lName);
        dest.writeString(profilePic);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(companyName);
        dest.writeString(gender);
        dest.writeString(description);
    }

    @Override
    public String toString() {
        return "TrainerBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lName='" + lName + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", companyName='" + companyName + '\'' +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
