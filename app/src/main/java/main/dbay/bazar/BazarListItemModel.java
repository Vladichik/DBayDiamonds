package main.dbay.bazar;

import java.util.Comparator;

/**
 * Created by vladvidavsky on 9/18/15.
 */
public class BazarListItemModel implements Comparable<BazarListItemModel> {
    String item, dateupdated, metal, central_stone, center, shape, color, clarity, total, certificate, description, sellername, sellerphone, sellermail, sellerskype, sellercountry, sellercity, price, image1, image2, image3, brand, model, box, document, advertid;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDateupdated() {
        return dateupdated;
    }

    public void setDateupdated(String dateupdated) {
        this.dateupdated = dateupdated;
    }

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public String getCentral_stone() {
        return central_stone;
    }

    public void setCentral_stone(String central_stone) {
        this.central_stone = central_stone;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getClarity() {
        return clarity;
    }

    public void setClarity(String clarity) {
        this.clarity = clarity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getSellerphone() {
        return sellerphone;
    }

    public void setSellerphone(String sellerphone) {
        this.sellerphone = sellerphone;
    }

    public String getSellermail() {
        return sellermail;
    }

    public void setSellermail(String sellermail) {
        this.sellermail = sellermail;
    }

    public String getSellerskype() {
        return sellerskype;
    }

    public void setSellerskype(String sellerskype) {
        this.sellerskype = sellerskype;
    }

    public String getSellercountry() {
        return sellercountry;
    }

    public void setSellercountry(String sellercountry) {
        this.sellercountry = sellercountry;
    }

    public String getSellercity() {
        return sellercity;
    }

    public void setSellercity(String sellercity) {
        this.sellercity = sellercity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getAdvertid() {
        return advertid;
    }

    public void setAdvertid(String advertid) {
        this.advertid = advertid;
    }

    public BazarListItemModel(String item, String dateupdated, String metal, String central_stone, String center, String shape, String color, String clarity, String total, String certificate, String description, String sellername, String sellerphone, String sellermail, String sellerskype, String sellercountry, String sellercity, String price, String image1, String image2, String image3, String brand, String model, String box, String document, String advertid) {
        this.item = item;
        this.dateupdated = dateupdated;
        this.metal = metal;
        this.central_stone = central_stone;
        this.center = center;
        this.shape = shape;
        this.color = color;
        this.clarity = clarity;
        this.total = total;
        this.certificate = certificate;

        this.description = description;
        this.sellername = sellername;
        this.sellerphone = sellerphone;
        this.sellermail = sellermail;
        this.sellerskype = sellerskype;
        this.sellercountry = sellercountry;
        this.sellercity = sellercity;
        this.price = price;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.brand = brand;
        this.model = model;
        this.box = box;
        this.document = document;
        this.advertid = advertid;
    }

    @Override
    public String toString() {
        return item + " " + dateupdated + " " + metal + " " + central_stone + " " + " " + center + " " + shape + " " + color + " " + clarity + " " + total + " " + certificate + " " + description + " " + sellername + " " + sellerphone + " " + sellermail + " " + sellerskype + " " + sellercountry + " " + sellercity + " " + price + " " + image1 + " " + image2 + " " + image3 + " " + brand + " " + model + " " + box + " " + document + " " + advertid;
    }


    @Override
    public int compareTo(BazarListItemModel another) {
        return Long.valueOf(another.dateupdated).compareTo(Long.valueOf(dateupdated));
    }

    public static Comparator<BazarListItemModel> COMPARE_BY_DATEUPDATED = new Comparator<BazarListItemModel>() {
        @Override
        public int compare(BazarListItemModel one, BazarListItemModel other) {
            return Long.valueOf(one.dateupdated).compareTo(Long.valueOf(other.dateupdated));
        }
    };
}
