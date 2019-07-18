
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChompProductBean implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("upc")
    @Expose
    private String upc;
    @SerializedName("details")
    @Expose
    private Details details;
    @SerializedName("api_endpoint")
    @Expose
    private String apiEndpoint;
    @SerializedName("serving_qty")
    @Expose
    private int servingQty;
    @SerializedName("serving_qty_second")
    @Expose
    private double servingQtySecond;

    protected ChompProductBean(Parcel in) {
        productId = in.readString();
        name = in.readString();
        manufacturer = in.readString();
        upc = in.readString();
        details = in.readParcelable(Details.class.getClassLoader());
        apiEndpoint = in.readString();
        servingQty = in.readInt();
        servingQtySecond = in.readDouble();
    }

    public static final Creator<ChompProductBean> CREATOR = new Creator<ChompProductBean>() {
        @Override
        public ChompProductBean createFromParcel(Parcel in) {
            return new ChompProductBean(in);
        }

        @Override
        public ChompProductBean[] newArray(int size) {
            return new ChompProductBean[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public int getServingQty() {
        return servingQty;
    }

    public void setServingQty(int servingQty) {
        this.servingQty = servingQty;
    }

    public double getServingQtySecond() {
        return servingQtySecond;
    }

    public void setServingQtySecond(double servingQtySecond) {
        this.servingQtySecond = servingQtySecond;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeString(manufacturer);
        dest.writeString(upc);
        dest.writeParcelable(details, flags);
        dest.writeString(apiEndpoint);
        dest.writeInt(servingQty);
        dest.writeDouble(servingQtySecond);
    }

    @Override
    public String toString() {
        return "ChompProductBean{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", upc='" + upc + '\'' +
                ", details=" + details +
                ", apiEndpoint='" + apiEndpoint + '\'' +
                ", servingQty=" + servingQty +
                ", servingQtySecond=" + servingQtySecond +
                '}';
    }
}
