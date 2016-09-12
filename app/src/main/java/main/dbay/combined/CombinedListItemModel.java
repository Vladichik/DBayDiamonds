package main.dbay.combined;

import java.util.Comparator;

/**
 * Created by vladvidavsky on 24/04/15.
 */
public class CombinedListItemModel implements Comparable<CombinedListItemModel>{
    String id, metal, details, certificate, price, image_big, cam_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_big() {
        return image_big;
    }

    public void setImage_big(String image_big) {
        this.image_big = image_big;
    }

    public String getCam_image() {
        return cam_image;
    }

    public void setCam_image(String cam_image) {
        this.cam_image = cam_image;
    }

    public CombinedListItemModel(String id, String metal, String details, String certificate, String price, String image_big, String cam_image) {
        this.id = id;
        this.metal = metal;
        this.details = details;
        this.certificate = certificate;
        this.price = price;
        this.image_big = image_big;
        this.cam_image = cam_image;
    }

    @Override
    public String toString() {
        return id + " " + metal + " " + details + " " + certificate + " " + price + " " + image_big + " " + cam_image;
    }

    @Override
    public int compareTo(CombinedListItemModel another) {
        return Integer.valueOf(price).compareTo(Integer.valueOf(another.price));
    }

    public static Comparator<CombinedListItemModel> COMPARE_BY_PRICE = new Comparator<CombinedListItemModel>() {
        @Override
        public int compare(CombinedListItemModel one, CombinedListItemModel other) {
            return Integer.valueOf(one.price).compareTo(Integer.valueOf(other.price));
        }
    };
}
