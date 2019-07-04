
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Keywords implements Parcelable {

    @SerializedName("bean")
    @Expose
    private String bean;
    @SerializedName("black")
    @Expose
    private String black;
    @SerializedName("burger")
    @Expose
    private String burger;
    @SerializedName("chipotle")
    @Expose
    private String chipotle;
    @SerializedName("gardein")
    @Expose
    private String gardein;
    @SerializedName("garden")
    @Expose
    private String garden;
    @SerializedName("inc")
    @Expose
    private String inc;
    @SerializedName("international")
    @Expose
    private String international;
    @SerializedName("protein")
    @Expose
    private String protein;

    protected Keywords(Parcel in) {
        bean = in.readString();
        black = in.readString();
        burger = in.readString();
        chipotle = in.readString();
        gardein = in.readString();
        garden = in.readString();
        inc = in.readString();
        international = in.readString();
        protein = in.readString();
    }

    public static final Creator<Keywords> CREATOR = new Creator<Keywords>() {
        @Override
        public Keywords createFromParcel(Parcel in) {
            return new Keywords(in);
        }

        @Override
        public Keywords[] newArray(int size) {
            return new Keywords[size];
        }
    };

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getBurger() {
        return burger;
    }

    public void setBurger(String burger) {
        this.burger = burger;
    }

    public String getChipotle() {
        return chipotle;
    }

    public void setChipotle(String chipotle) {
        this.chipotle = chipotle;
    }

    public String getGardein() {
        return gardein;
    }

    public void setGardein(String gardein) {
        this.gardein = gardein;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getInc() {
        return inc;
    }

    public void setInc(String inc) {
        this.inc = inc;
    }

    public String getInternational() {
        return international;
    }

    public void setInternational(String international) {
        this.international = international;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bean);
        dest.writeString(black);
        dest.writeString(burger);
        dest.writeString(chipotle);
        dest.writeString(gardein);
        dest.writeString(garden);
        dest.writeString(inc);
        dest.writeString(international);
        dest.writeString(protein);
    }

    @Override
    public String toString() {
        return "Keywords{" +
                "bean='" + bean + '\'' +
                ", black='" + black + '\'' +
                ", burger='" + burger + '\'' +
                ", chipotle='" + chipotle + '\'' +
                ", gardein='" + gardein + '\'' +
                ", garden='" + garden + '\'' +
                ", inc='" + inc + '\'' +
                ", international='" + international + '\'' +
                ", protein='" + protein + '\'' +
                '}';
    }
}
