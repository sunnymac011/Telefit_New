
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images implements Parcelable {

    @SerializedName("front")
    @Expose
    private Front front;
    @SerializedName("nutrition")
    @Expose
    private Nutrition nutrition;
    @SerializedName("ingredients")
    @Expose
    private Ingredients ingredients;

    protected Images(Parcel in) {
        front = in.readParcelable(Front.class.getClassLoader());
        nutrition = in.readParcelable(Nutrition.class.getClassLoader());
        ingredients = in.readParcelable(Ingredients.class.getClassLoader());
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(front, flags);
        dest.writeParcelable(nutrition, flags);
        dest.writeParcelable(ingredients, flags);
    }

    @Override
    public String toString() {
        return "Images{" +
                "front=" + front +
                ", nutrition=" + nutrition +
                ", ingredients=" + ingredients +
                '}';
    }
}
