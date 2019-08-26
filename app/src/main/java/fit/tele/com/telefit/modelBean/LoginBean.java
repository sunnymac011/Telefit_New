
package fit.tele.com.telefit.modelBean;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBean extends BaseObservable implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("profile_pic_url")
    @Expose
    private String profilePic;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("social_id")
    @Expose
    private Object socialId;
    @SerializedName("session_token")
    @Expose
    private String sessionToken;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("bdate")
    @Expose
    private String dob;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("login_by")
    @Expose
    private String loginBy;
    @SerializedName("is_social")
    @Expose
    private String isSocial;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("register_status")
    @Expose
    private Integer registerStatus;
    @SerializedName("is_email_verify")
    @Expose
    private String isEmailVerify;
    @SerializedName("is_delete")
    @Expose
    private Integer isDelete;
    @SerializedName("forgot_otp")
    @Expose
    private Object forgotOtp;

    @SerializedName("Is_friend_share")
    @Expose
    private String Is_friend_share;
    @SerializedName("Is_trainer_share")
    @Expose
    private String Is_trainer_share;
    @SerializedName("Is_facebook_share")
    @Expose
    private String Is_facebook_share;
    @SerializedName("Is_twiter_share")
    @Expose
    private String Is_twiter_share;
    @SerializedName("Is_instagram_share")
    @Expose
    private String Is_instagram_share;
    @SerializedName("Is_snapchat_share")
    @Expose
    private String Is_snapchat_share;


    protected LoginBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        lName = in.readString();
        profilePic = in.readString();
        email = in.readString();
        sessionToken = in.readString();
        gender = in.readString();
        address = in.readString();
        dob = in.readString();
        height = in.readString();
        weight = in.readString();
        deviceToken = in.readString();
        deviceType = in.readString();
        loginBy = in.readString();
        isSocial = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            registerStatus = null;
        } else {
            registerStatus = in.readInt();
        }
        isEmailVerify = in.readString();
        if (in.readByte() == 0) {
            isDelete = null;
        } else {
            isDelete = in.readInt();
        }
        Is_friend_share = in.readString();
        Is_trainer_share = in.readString();
        Is_facebook_share = in.readString();
        Is_twiter_share = in.readString();
        Is_instagram_share = in.readString();
        Is_snapchat_share = in.readString();
    }

    public static final Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLoginBy() {
        return loginBy;
    }

    public void setLoginBy(String loginBy) {
        this.loginBy = loginBy;
    }

    public String getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(String isSocial) {
        this.isSocial = isSocial;
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

    public Integer getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(Integer registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getIsEmailVerify() {
        return isEmailVerify;
    }

    public void setIsEmailVerify(String isEmailVerify) {
        this.isEmailVerify = isEmailVerify;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Object getForgotOtp() {
        return forgotOtp;
    }

    public void setForgotOtp(Object forgotOtp) {
        this.forgotOtp = forgotOtp;
    }

    public String getIs_friend_share() {
        return Is_friend_share;
    }

    public void setIs_friend_share(String is_friend_share) {
        Is_friend_share = is_friend_share;
    }

    public String getIs_trainer_share() {
        return Is_trainer_share;
    }

    public void setIs_trainer_share(String is_trainer_share) {
        Is_trainer_share = is_trainer_share;
    }

    public String getIs_facebook_share() {
        return Is_facebook_share;
    }

    public void setIs_facebook_share(String is_facebook_share) {
        Is_facebook_share = is_facebook_share;
    }

    public String getIs_twiter_share() {
        return Is_twiter_share;
    }

    public void setIs_twiter_share(String is_twiter_share) {
        Is_twiter_share = is_twiter_share;
    }

    public String getIs_instagram_share() {
        return Is_instagram_share;
    }

    public void setIs_instagram_share(String is_instagram_share) {
        Is_instagram_share = is_instagram_share;
    }

    public String getIs_snapchat_share() {
        return Is_snapchat_share;
    }

    public void setIs_snapchat_share(String is_snapchat_share) {
        Is_snapchat_share = is_snapchat_share;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(lName);
        dest.writeString(profilePic);
        dest.writeString(email);
        dest.writeString(sessionToken);
        dest.writeString(gender);
        dest.writeString(address);
        dest.writeString(dob);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(deviceToken);
        dest.writeString(deviceType);
        dest.writeString(loginBy);
        dest.writeString(isSocial);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (registerStatus == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(registerStatus);
        }
        dest.writeString(isEmailVerify);
        if (isDelete == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isDelete);
        }
        dest.writeString(Is_friend_share);
        dest.writeString(Is_trainer_share);
        dest.writeString(Is_facebook_share);
        dest.writeString(Is_twiter_share);
        dest.writeString(Is_instagram_share);
        dest.writeString(Is_snapchat_share);
    }
}
