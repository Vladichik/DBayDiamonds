package main.dbay;

import java.util.Comparator;

/**
 * Created by vladvidavsky on 9/04/15.
 */
public class JewelleryListItemModel implements Comparable<JewelleryListItemModel> {
    String itemId, metal, total, center, diamColor, clarity, certificate, price, image, cam_image;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getDiamColor() {
        return diamColor;
    }

    public void setDiamColor(String diamColor) {
        this.diamColor = diamColor;
    }

    public String getClarity() {
        return clarity;
    }

    public void setClarity(String clarity) {
        this.clarity = clarity;
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

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public String getCam_image() {
        return cam_image;
    }

    public void setCam_image(String cam_image) {
        this.cam_image = cam_image;
    }

    public JewelleryListItemModel(String itemId, String metal, String total, String center, String diamColor, String clarity, String certificate, String price, String image, String cam_image) {
        this.itemId = itemId;
        this.metal = metal;
        this.total = total;
        this.center = center;
        this.diamColor = diamColor;
        this.clarity = clarity;
        this.certificate = certificate;
        this.price = price;
        this.image = image;
        this.cam_image = cam_image;
    }

    @Override
    public String toString() {
        return itemId + " " + metal + " " + total + " " + center + " " + diamColor + " " + clarity + " " + certificate + " " + price + " " + image + " " + cam_image;
    }

    @Override
    public int compareTo(JewelleryListItemModel another) {
        return Integer.valueOf(price).compareTo(Integer.valueOf(another.price));
    }

    public static Comparator<JewelleryListItemModel> COMPARE_BY_PRICE = new Comparator<JewelleryListItemModel>() {
        @Override
        public int compare(JewelleryListItemModel one, JewelleryListItemModel other) {
            return Integer.valueOf(one.price).compareTo(Integer.valueOf(other.price));
        }
    };
}
