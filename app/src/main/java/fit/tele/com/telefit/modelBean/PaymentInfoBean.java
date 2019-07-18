
package fit.tele.com.telefit.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.Package;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInfoBean implements Parcelable {

    @SerializedName("terms")
    @Expose
    private TermsBean terms;
    @SerializedName("package")
    @Expose
    private ArrayList<PackageBean> usePackage = null;
    @SerializedName("is_package")
    @Expose
    private String isPackage;

    protected PaymentInfoBean(Parcel in) {
        terms = in.readParcelable(TermsBean.class.getClassLoader());
        usePackage = in.createTypedArrayList(PackageBean.CREATOR);
        isPackage = in.readString();
    }

    public static final Creator<PaymentInfoBean> CREATOR = new Creator<PaymentInfoBean>() {
        @Override
        public PaymentInfoBean createFromParcel(Parcel in) {
            return new PaymentInfoBean(in);
        }

        @Override
        public PaymentInfoBean[] newArray(int size) {
            return new PaymentInfoBean[size];
        }
    };

    public TermsBean getTerms() {
        return terms;
    }

    public void setTerms(TermsBean terms) {
        this.terms = terms;
    }

    public ArrayList<PackageBean> getUsePackage() {
        return usePackage;
    }

    public void setUsePackage(ArrayList<PackageBean> usePackage) {
        this.usePackage = usePackage;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(terms, flags);
        dest.writeTypedList(usePackage);
        dest.writeString(isPackage);
    }

    @Override
    public String toString() {
        return "PaymentInfoBean{" +
                "terms=" + terms +
                ", usePackage=" + usePackage +
                ", isPackage='" + isPackage + '\'' +
                '}';
    }
}
