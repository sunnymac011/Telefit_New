package fit.tele.com.telefit.modelBean.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String user_id;
  //  private String chat_id;
  //  private String job_id_new;
    private String name;
   // private String title;
    private String photo_profile;
    private String friend_id;

    public UserModel() {
    }

    public UserModel(String name, String photo_profile, String user_id,String friend_id) {
        this.name = name;
        this.photo_profile = photo_profile;
        this.user_id = user_id;
        this.friend_id = friend_id;

    }

    protected UserModel(Parcel in) {
        user_id = in.readString();
        name = in.readString();
        photo_profile = in.readString();
        friend_id = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user_id);
        parcel.writeString(name);
        parcel.writeString(photo_profile);
        parcel.writeString(friend_id);
    }
}
