
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lifestyle implements Parcelable {

    @SerializedName("vegan")
    @Expose
    private Vegan vegan;
    @SerializedName("vegetarian")
    @Expose
    private Vegetarian vegetarian;
    @SerializedName("gluten-free")
    @Expose
    private GlutenFree glutenFree;

    protected Lifestyle(Parcel in) {
        vegan = in.readParcelable(Vegan.class.getClassLoader());
        vegetarian = in.readParcelable(Vegetarian.class.getClassLoader());
        glutenFree = in.readParcelable(GlutenFree.class.getClassLoader());
    }

    public static final Creator<Lifestyle> CREATOR = new Creator<Lifestyle>() {
        @Override
        public Lifestyle createFromParcel(Parcel in) {
            return new Lifestyle(in);
        }

        @Override
        public Lifestyle[] newArray(int size) {
            return new Lifestyle[size];
        }
    };

    public Vegan getVegan() {
        return vegan;
    }

    public void setVegan(Vegan vegan) {
        this.vegan = vegan;
    }

    public Vegetarian getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Vegetarian vegetarian) {
        this.vegetarian = vegetarian;
    }

    public GlutenFree getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(GlutenFree glutenFree) {
        this.glutenFree = glutenFree;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(vegan, flags);
        dest.writeParcelable(vegetarian, flags);
        dest.writeParcelable(glutenFree, flags);
    }

    @Override
    public String toString() {
        return "Lifestyle{" +
                "vegan=" + vegan +
                ", vegetarian=" + vegetarian +
                ", glutenFree=" + glutenFree +
                '}';
    }
}
