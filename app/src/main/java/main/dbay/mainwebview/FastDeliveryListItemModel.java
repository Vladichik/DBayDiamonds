package main.dbay.mainwebview;

/**
 * Created by vladvidavsky on 2/05/15.
 */
public class FastDeliveryListItemModel {
    String id, image, metal, description, certificate, price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public FastDeliveryListItemModel(String id, String image, String metal, String description, String certificate, String price) {
        this.id = id;
        this.image = image;
        this.metal = metal;
        this.description = description;
        this.certificate = certificate;
        this.price = price;
    }

    @Override
    public String toString() {
        return id + " " + image + " " + metal + " " + description + " " + certificate + " " + price;
    }
}
