package main.dbay.diamonds;

import java.util.Comparator;

/**
 * Created by vladvidavsky on 5/04/15.
 */
public class DiamondsListItemModel implements Comparable<DiamondsListItemModel> {
    String shape, weight, color, clarity, polish, symmetry, fluoricence, certificate, dimmentions;

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(String symmetry) {
        this.symmetry = symmetry;
    }

    public String getFluoricence() {
        return fluoricence;
    }

    public void setFluoricence(String fluoricence) {
        this.fluoricence = fluoricence;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getDimmentions() {
        return dimmentions;
    }

    public void setDimmentions(String dimmentions) {
        this.dimmentions = dimmentions;
    }

    public DiamondsListItemModel(String shape, String weight, String color, String clarity, String polish, String symmetry, String fluoricence, String certificate, String dimmentions) {
        this.shape = shape;
        this.weight = weight;
        this.color = color;
        this.clarity = clarity;
        this.polish = polish;
        this.symmetry = symmetry;
        this.fluoricence = fluoricence;
        this.certificate = certificate;
        this.dimmentions = dimmentions;
    }

    @Override
    public String toString() {
        return shape + " " + weight + " " + color + " " + clarity + " " + polish + " " + symmetry + " " + fluoricence + " " + certificate + " " + dimmentions;
    }

    @Override
    public int compareTo(DiamondsListItemModel another) {
        return weight.compareTo(another.weight);
    }

    public static Comparator<DiamondsListItemModel> COMPARE_BY_WEIGHT = new Comparator<DiamondsListItemModel>() {
        @Override
        public int compare(DiamondsListItemModel one, DiamondsListItemModel other) {
            return one.weight.compareTo(other.weight);
        }
    };
}
