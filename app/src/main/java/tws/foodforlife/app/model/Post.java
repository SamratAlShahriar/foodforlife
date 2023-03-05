package tws.foodforlife.app.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import tws.foodforlife.app.firebase.POSTCONS;

public class Post {
    @PropertyName(POSTCONS.P_ID)
    private String pId;

    @PropertyName(POSTCONS.P_POSTED_BY_ID)
    private String postedById;

    @PropertyName(POSTCONS.P_POSTED_BY_NAME)
    private String postedByName;

    @PropertyName(POSTCONS.P_POSTED_BY_IMAGE_URL)
    private String postedByImageUrl;

    @PropertyName(POSTCONS.P_ACCEPTED_BY_ID)
    private String acceptedById;

    @PropertyName(POSTCONS.P_GEO_POINT)
    private GeoPoint pGeoPoint;

    @PropertyName(POSTCONS.P_LOCATION_NAME)
    private String pLocationName;

    @PropertyName(POSTCONS.P_TYPE)
    private String pType;

    @PropertyName(POSTCONS.P_IMAGE_URL)
    private String pImageUrl;

    @PropertyName(POSTCONS.P_STATUS)
    private String status;

    @PropertyName(POSTCONS.P_PHONE_NO)
    private String phoneNo;

    @PropertyName(POSTCONS.P_MAIN_POST)
    private String mainPost;


    @PropertyName(POSTCONS.P_TIMESTAMP)
    @ServerTimestamp
    private Timestamp timestamp;


    public Post() {
    }

    @PropertyName(POSTCONS.P_ID)
    public String getpId() {
        return pId;
    }

    @PropertyName(POSTCONS.P_ID)
    public void setpId(String pId) {
        this.pId = pId;
    }

    @PropertyName(POSTCONS.P_POSTED_BY_ID)
    public String getPostedById() {
        return postedById;
    }

    @PropertyName(POSTCONS.P_POSTED_BY_ID)
    public void setPostedById(String postedById) {
        this.postedById = postedById;
    }

    @PropertyName(POSTCONS.P_POSTED_BY_NAME)
    public String getPostedByName() {
        return postedByName;
    }

    @PropertyName(POSTCONS.P_POSTED_BY_NAME)
    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    @PropertyName(POSTCONS.P_POSTED_BY_IMAGE_URL)
    public String getPostedByImageUrl() {
        return postedByImageUrl;
    }

    @PropertyName(POSTCONS.P_POSTED_BY_IMAGE_URL)
    public void setPostedByImageUrl(String postedByImageUrl) {
        this.postedByImageUrl = postedByImageUrl;
    }

    @PropertyName(POSTCONS.P_ACCEPTED_BY_ID)
    public String getAcceptedById() {
        return acceptedById;
    }

    @PropertyName(POSTCONS.P_ACCEPTED_BY_ID)
    public void setAcceptedById(String acceptedById) {
        this.acceptedById = acceptedById;
    }

    @PropertyName(POSTCONS.P_GEO_POINT)
    public GeoPoint getpGeoPoint() {
        return pGeoPoint;
    }

    @PropertyName(POSTCONS.P_GEO_POINT)
    public void setpGeoPoint(GeoPoint pGeoPoint) {
        this.pGeoPoint = pGeoPoint;
    }

    @PropertyName(POSTCONS.P_LOCATION_NAME)
    public String getpLocationName() {
        return pLocationName;
    }

    @PropertyName(POSTCONS.P_LOCATION_NAME)
    public void setpLocationName(String pLocationName) {
        this.pLocationName = pLocationName;
    }

    @PropertyName(POSTCONS.P_TYPE)
    public String getpType() {
        return pType;
    }

    @PropertyName(POSTCONS.P_TYPE)
    public void setpType(String pType) {
        this.pType = pType;
    }

    @PropertyName(POSTCONS.P_IMAGE_URL)
    public String getpImageUrl() {
        return pImageUrl;
    }
    @PropertyName(POSTCONS.P_IMAGE_URL)
    public void setpImageUrl(String pImageUrl) {
        this.pImageUrl = pImageUrl;
    }

    @PropertyName(POSTCONS.P_STATUS)
    public String getStatus() {
        return status;
    }

    @PropertyName(POSTCONS.P_STATUS)
    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName(POSTCONS.P_PHONE_NO)
    public String getPhoneNo() {
        return phoneNo;
    }

    @PropertyName(POSTCONS.P_PHONE_NO)
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @PropertyName(POSTCONS.P_MAIN_POST)
    public String getMainPost() {
        return mainPost;
    }

    @PropertyName(POSTCONS.P_MAIN_POST)
    public void setMainPost(String mainPost) {
        this.mainPost = mainPost;
    }

    @PropertyName(POSTCONS.P_TIMESTAMP)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @PropertyName(POSTCONS.P_TIMESTAMP)
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
