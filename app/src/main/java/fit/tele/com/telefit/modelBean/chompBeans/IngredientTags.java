
package fit.tele.com.telefit.modelBean.chompBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IngredientTags implements Parcelable {

    @SerializedName("black-bean-powder")
    @Expose
    private String blackBeanPowder;
    @SerializedName("citric-acid")
    @Expose
    private String citricAcid;
    @SerializedName("cooked-black-beans")
    @Expose
    private String cookedBlackBeans;
    @SerializedName("cooked-brown-rice")
    @Expose
    private String cookedBrownRice;
    @SerializedName("expeller-pressed-canola-oil")
    @Expose
    private String expellerPressedCanolaOil;
    @SerializedName("from-plant-sources")
    @Expose
    private String fromPlantSources;
    @SerializedName("garlic-powder")
    @Expose
    private String garlicPowder;
    @SerializedName("instant-coffee")
    @Expose
    private String instantCoffee;
    @SerializedName("lactic-acid")
    @Expose
    private String lacticAcid;
    @SerializedName("modified-vegetable-gum")
    @Expose
    private String modifiedVegetableGum;
    @SerializedName("molasses")
    @Expose
    private String molasses;
    @SerializedName("natural-flavors")
    @Expose
    private String naturalFlavors;
    @SerializedName("onion")
    @Expose
    private String onion;
    @SerializedName("onion-powder")
    @Expose
    private String onionPowder;
    @SerializedName("organic-cane-sugar")
    @Expose
    private String organicCaneSugar;
    @SerializedName("paprika")
    @Expose
    private String paprika;
    @SerializedName("potato-starch")
    @Expose
    private String potatoStarch;
    @SerializedName("roasted-red-and-green-bell-peppers")
    @Expose
    private String roastedRedAndGreenBellPeppers;
    @SerializedName("roasted-yellow-corn")
    @Expose
    private String roastedYellowCorn;
    @SerializedName("sea-salt")
    @Expose
    private String seaSalt;
    @SerializedName("soy-protein-concentrate")
    @Expose
    private String soyProteinConcentrate;
    @SerializedName("spices")
    @Expose
    private String spices;
    @SerializedName("tomato-paste")
    @Expose
    private String tomatoPaste;
    @SerializedName("tomato-powder")
    @Expose
    private String tomatoPowder;
    @SerializedName("water")
    @Expose
    private String water;
    @SerializedName("yeast-extract")
    @Expose
    private String yeastExtract;

    protected IngredientTags(Parcel in) {
        blackBeanPowder = in.readString();
        citricAcid = in.readString();
        cookedBlackBeans = in.readString();
        cookedBrownRice = in.readString();
        expellerPressedCanolaOil = in.readString();
        fromPlantSources = in.readString();
        garlicPowder = in.readString();
        instantCoffee = in.readString();
        lacticAcid = in.readString();
        modifiedVegetableGum = in.readString();
        molasses = in.readString();
        naturalFlavors = in.readString();
        onion = in.readString();
        onionPowder = in.readString();
        organicCaneSugar = in.readString();
        paprika = in.readString();
        potatoStarch = in.readString();
        roastedRedAndGreenBellPeppers = in.readString();
        roastedYellowCorn = in.readString();
        seaSalt = in.readString();
        soyProteinConcentrate = in.readString();
        spices = in.readString();
        tomatoPaste = in.readString();
        tomatoPowder = in.readString();
        water = in.readString();
        yeastExtract = in.readString();
    }

    public static final Creator<IngredientTags> CREATOR = new Creator<IngredientTags>() {
        @Override
        public IngredientTags createFromParcel(Parcel in) {
            return new IngredientTags(in);
        }

        @Override
        public IngredientTags[] newArray(int size) {
            return new IngredientTags[size];
        }
    };

    public String getBlackBeanPowder() {
        return blackBeanPowder;
    }

    public void setBlackBeanPowder(String blackBeanPowder) {
        this.blackBeanPowder = blackBeanPowder;
    }

    public String getCitricAcid() {
        return citricAcid;
    }

    public void setCitricAcid(String citricAcid) {
        this.citricAcid = citricAcid;
    }

    public String getCookedBlackBeans() {
        return cookedBlackBeans;
    }

    public void setCookedBlackBeans(String cookedBlackBeans) {
        this.cookedBlackBeans = cookedBlackBeans;
    }

    public String getCookedBrownRice() {
        return cookedBrownRice;
    }

    public void setCookedBrownRice(String cookedBrownRice) {
        this.cookedBrownRice = cookedBrownRice;
    }

    public String getExpellerPressedCanolaOil() {
        return expellerPressedCanolaOil;
    }

    public void setExpellerPressedCanolaOil(String expellerPressedCanolaOil) {
        this.expellerPressedCanolaOil = expellerPressedCanolaOil;
    }

    public String getFromPlantSources() {
        return fromPlantSources;
    }

    public void setFromPlantSources(String fromPlantSources) {
        this.fromPlantSources = fromPlantSources;
    }

    public String getGarlicPowder() {
        return garlicPowder;
    }

    public void setGarlicPowder(String garlicPowder) {
        this.garlicPowder = garlicPowder;
    }

    public String getInstantCoffee() {
        return instantCoffee;
    }

    public void setInstantCoffee(String instantCoffee) {
        this.instantCoffee = instantCoffee;
    }

    public String getLacticAcid() {
        return lacticAcid;
    }

    public void setLacticAcid(String lacticAcid) {
        this.lacticAcid = lacticAcid;
    }

    public String getModifiedVegetableGum() {
        return modifiedVegetableGum;
    }

    public void setModifiedVegetableGum(String modifiedVegetableGum) {
        this.modifiedVegetableGum = modifiedVegetableGum;
    }

    public String getMolasses() {
        return molasses;
    }

    public void setMolasses(String molasses) {
        this.molasses = molasses;
    }

    public String getNaturalFlavors() {
        return naturalFlavors;
    }

    public void setNaturalFlavors(String naturalFlavors) {
        this.naturalFlavors = naturalFlavors;
    }

    public String getOnion() {
        return onion;
    }

    public void setOnion(String onion) {
        this.onion = onion;
    }

    public String getOnionPowder() {
        return onionPowder;
    }

    public void setOnionPowder(String onionPowder) {
        this.onionPowder = onionPowder;
    }

    public String getOrganicCaneSugar() {
        return organicCaneSugar;
    }

    public void setOrganicCaneSugar(String organicCaneSugar) {
        this.organicCaneSugar = organicCaneSugar;
    }

    public String getPaprika() {
        return paprika;
    }

    public void setPaprika(String paprika) {
        this.paprika = paprika;
    }

    public String getPotatoStarch() {
        return potatoStarch;
    }

    public void setPotatoStarch(String potatoStarch) {
        this.potatoStarch = potatoStarch;
    }

    public String getRoastedRedAndGreenBellPeppers() {
        return roastedRedAndGreenBellPeppers;
    }

    public void setRoastedRedAndGreenBellPeppers(String roastedRedAndGreenBellPeppers) {
        this.roastedRedAndGreenBellPeppers = roastedRedAndGreenBellPeppers;
    }

    public String getRoastedYellowCorn() {
        return roastedYellowCorn;
    }

    public void setRoastedYellowCorn(String roastedYellowCorn) {
        this.roastedYellowCorn = roastedYellowCorn;
    }

    public String getSeaSalt() {
        return seaSalt;
    }

    public void setSeaSalt(String seaSalt) {
        this.seaSalt = seaSalt;
    }

    public String getSoyProteinConcentrate() {
        return soyProteinConcentrate;
    }

    public void setSoyProteinConcentrate(String soyProteinConcentrate) {
        this.soyProteinConcentrate = soyProteinConcentrate;
    }

    public String getSpices() {
        return spices;
    }

    public void setSpices(String spices) {
        this.spices = spices;
    }

    public String getTomatoPaste() {
        return tomatoPaste;
    }

    public void setTomatoPaste(String tomatoPaste) {
        this.tomatoPaste = tomatoPaste;
    }

    public String getTomatoPowder() {
        return tomatoPowder;
    }

    public void setTomatoPowder(String tomatoPowder) {
        this.tomatoPowder = tomatoPowder;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getYeastExtract() {
        return yeastExtract;
    }

    public void setYeastExtract(String yeastExtract) {
        this.yeastExtract = yeastExtract;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(blackBeanPowder);
        dest.writeString(citricAcid);
        dest.writeString(cookedBlackBeans);
        dest.writeString(cookedBrownRice);
        dest.writeString(expellerPressedCanolaOil);
        dest.writeString(fromPlantSources);
        dest.writeString(garlicPowder);
        dest.writeString(instantCoffee);
        dest.writeString(lacticAcid);
        dest.writeString(modifiedVegetableGum);
        dest.writeString(molasses);
        dest.writeString(naturalFlavors);
        dest.writeString(onion);
        dest.writeString(onionPowder);
        dest.writeString(organicCaneSugar);
        dest.writeString(paprika);
        dest.writeString(potatoStarch);
        dest.writeString(roastedRedAndGreenBellPeppers);
        dest.writeString(roastedYellowCorn);
        dest.writeString(seaSalt);
        dest.writeString(soyProteinConcentrate);
        dest.writeString(spices);
        dest.writeString(tomatoPaste);
        dest.writeString(tomatoPowder);
        dest.writeString(water);
        dest.writeString(yeastExtract);
    }

    @Override
    public String toString() {
        return "IngredientTags{" +
                "blackBeanPowder='" + blackBeanPowder + '\'' +
                ", citricAcid='" + citricAcid + '\'' +
                ", cookedBlackBeans='" + cookedBlackBeans + '\'' +
                ", cookedBrownRice='" + cookedBrownRice + '\'' +
                ", expellerPressedCanolaOil='" + expellerPressedCanolaOil + '\'' +
                ", fromPlantSources='" + fromPlantSources + '\'' +
                ", garlicPowder='" + garlicPowder + '\'' +
                ", instantCoffee='" + instantCoffee + '\'' +
                ", lacticAcid='" + lacticAcid + '\'' +
                ", modifiedVegetableGum='" + modifiedVegetableGum + '\'' +
                ", molasses='" + molasses + '\'' +
                ", naturalFlavors='" + naturalFlavors + '\'' +
                ", onion='" + onion + '\'' +
                ", onionPowder='" + onionPowder + '\'' +
                ", organicCaneSugar='" + organicCaneSugar + '\'' +
                ", paprika='" + paprika + '\'' +
                ", potatoStarch='" + potatoStarch + '\'' +
                ", roastedRedAndGreenBellPeppers='" + roastedRedAndGreenBellPeppers + '\'' +
                ", roastedYellowCorn='" + roastedYellowCorn + '\'' +
                ", seaSalt='" + seaSalt + '\'' +
                ", soyProteinConcentrate='" + soyProteinConcentrate + '\'' +
                ", spices='" + spices + '\'' +
                ", tomatoPaste='" + tomatoPaste + '\'' +
                ", tomatoPowder='" + tomatoPowder + '\'' +
                ", water='" + water + '\'' +
                ", yeastExtract='" + yeastExtract + '\'' +
                '}';
    }
}
