package tws.foodforlife.app.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import tws.foodforlife.app.firebase.RATICONS;

public class Rating {
    @PropertyName(RATICONS.RA_ID)
    private String ratId;

    @PropertyName(RATICONS.RA_GIVEN_TO_ID)
    private String ratGivenToId;

    @PropertyName(RATICONS.RA_GIVEN_BY_ID)
    private String ratGivenById;

    @PropertyName(RATICONS.RA_TING)
    private double rating;

    @ServerTimestamp
    @PropertyName(RATICONS.RA_TIMESTAMP)
    private Timestamp timestamp;

    public Rating() {
    }

    @PropertyName(RATICONS.RA_ID)
    public String getRatId() {
        return ratId;
    }

    @PropertyName(RATICONS.RA_ID)
    public void setRatId(String ratId) {
        this.ratId = ratId;
    }

    @PropertyName(RATICONS.RA_GIVEN_TO_ID)
    public String getRatGivenToId() {
        return ratGivenToId;
    }

    @PropertyName(RATICONS.RA_GIVEN_TO_ID)
    public void setRatGivenToId(String ratGivenToId) {
        this.ratGivenToId = ratGivenToId;
    }

    @PropertyName(RATICONS.RA_GIVEN_BY_ID)
    public String getRatGivenById() {
        return ratGivenById;
    }

    @PropertyName(RATICONS.RA_GIVEN_BY_ID)
    public void setRatGivenById(String ratGivenById) {
        this.ratGivenById = ratGivenById;
    }

    @PropertyName(RATICONS.RA_TING)
    public double getRating() {
        return rating;
    }

    @PropertyName(RATICONS.RA_TING)
    public void setRating(double rating) {
        this.rating = rating;
    }

    @PropertyName(RATICONS.RA_TIMESTAMP)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @PropertyName(RATICONS.RA_TIMESTAMP)
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
