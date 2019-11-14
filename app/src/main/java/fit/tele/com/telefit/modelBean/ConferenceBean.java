
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceBean implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("trainer_id")
    @Expose
    public String trainerId;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    @SerializedName("Is_accept")
    @Expose
    public String isAccept;
    @SerializedName("triner_comment")
    @Expose
    public String trinerComment;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("l_name")
    @Expose
    public String lName;
    @SerializedName("avatar")
    @Expose
    public String avatar;

    protected ConferenceBean(Parcel in) {
        id = in.readString();
        userId = in.readString();
        trainerId = in.readString();
        date = in.readString();
        time = in.readString();
        dateTime = in.readString();
        isAccept = in.readString();
        trinerComment = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        name = in.readString();
        lName = in.readString();
        avatar = in.readString();
    }

    public static final Creator<ConferenceBean> CREATOR = new Creator<ConferenceBean>() {
        @Override
        public ConferenceBean createFromParcel(Parcel in) {
            return new ConferenceBean(in);
        }

        @Override
        public ConferenceBean[] newArray(int size) {
            return new ConferenceBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }

    public String getTrinerComment() {
        return trinerComment;
    }

    public void setTrinerComment(String trinerComment) {
        this.trinerComment = trinerComment;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(trainerId);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(dateTime);
        dest.writeString(isAccept);
        dest.writeString(trinerComment);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(name);
        dest.writeString(lName);
        dest.writeString(avatar);
    }

    @Override
    public String toString() {
        return "ConferenceBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", isAccept='" + isAccept + '\'' +
                ", trinerComment='" + trinerComment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", name='" + name + '\'' +
                ", lName='" + lName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
