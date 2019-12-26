
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeListBean implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("racipe_calories")
    @Expose
    private String racipeCalories;
    @SerializedName("total_calories")
    @Expose
    private String totalCalories;
    @SerializedName("image")
    @Expose
    private String image;

    public RecipeListBean() {
    }

    protected RecipeListBean(Parcel in) {
        id = in.readString();
        name = in.readString();
        racipeCalories = in.readString();
        totalCalories = in.readString();
        image = in.readString();
    }

    public static final Creator<RecipeListBean> CREATOR = new Creator<RecipeListBean>() {
        @Override
        public RecipeListBean createFromParcel(Parcel in) {
            return new RecipeListBean(in);
        }

        @Override
        public RecipeListBean[] newArray(int size) {
            return new RecipeListBean[size];
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

    public String getRacipeCalories() {
        return racipeCalories;
    }

    public void setRacipeCalories(String racipeCalories) {
        this.racipeCalories = racipeCalories;
    }

    public String getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(String totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(racipeCalories);
        dest.writeString(totalCalories);
        dest.writeString(image);
    }

    @Override
    public String toString() {
        return "RecipeListBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", racipeCalories='" + racipeCalories + '\'' +
                ", totalCalories='" + totalCalories + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
