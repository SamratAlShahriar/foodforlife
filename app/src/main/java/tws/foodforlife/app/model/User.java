package tws.foodforlife.app.model;

import com.google.firebase.firestore.PropertyName;

import tws.foodforlife.app.firebase.USERCONS;

public class User {
    @PropertyName(USERCONS.UID)
    private String uid;

    @PropertyName(USERCONS.NAME)
    private String name;

    @PropertyName(USERCONS.EMAIL)
    private String email;

    @PropertyName(USERCONS.PHONE)
    private String phone;

    @PropertyName(USERCONS.DOB)
    private String dateOfBirth;

    @PropertyName(USERCONS.ORG_REG_NO)
    private String registrationNo;

    @PropertyName(USERCONS.ADDRESS)
    private String address;

    @PropertyName(USERCONS.USER_TYPE)
    private String userType;

    @PropertyName(USERCONS.USER_IMAGE_URL)
    private String userImageUrl;

    @PropertyName(USERCONS.IS_EMAIL_VERIFIED)
    private boolean isEmailVerified;

    @PropertyName(USERCONS.IS_ORG_VERIFIED)
    private boolean isOrgVerified;



    public User() {
    }


    @PropertyName(USERCONS.UID)
    public String getUid() {
        return uid;
    }

    @PropertyName(USERCONS.UID)
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName(USERCONS.NAME)
    public String getName() {
        return name;
    }

    @PropertyName(USERCONS.NAME)
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName(USERCONS.EMAIL)
    public String getEmail() {
        return email;
    }

    @PropertyName(USERCONS.EMAIL)
    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName(USERCONS.PHONE)
    public String getPhone() {
        return phone;
    }

    @PropertyName(USERCONS.PHONE)
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @PropertyName(USERCONS.DOB)
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @PropertyName(USERCONS.DOB)
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @PropertyName(USERCONS.ORG_REG_NO)
    public String getRegistrationNo() {
        return registrationNo;
    }

    @PropertyName(USERCONS.ORG_REG_NO)
    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    @PropertyName(USERCONS.ADDRESS)
    public String getAddress() {
        return address;
    }
    @PropertyName(USERCONS.ADDRESS)
    public void setAddress(String address) {
        this.address = address;
    }

    @PropertyName(USERCONS.USER_TYPE)
    public String getUserType() {
        return userType;
    }

    @PropertyName(USERCONS.USER_TYPE)
    public void setUserType(String userType) {
        this.userType = userType;
    }

    @PropertyName(USERCONS.USER_IMAGE_URL)
    public String getUserImageUrl() {
        return userImageUrl;
    }

    @PropertyName(USERCONS.USER_IMAGE_URL)
    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @PropertyName(USERCONS.IS_EMAIL_VERIFIED)
    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    @PropertyName(USERCONS.IS_EMAIL_VERIFIED)
    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    @PropertyName(USERCONS.IS_ORG_VERIFIED)
    public boolean isOrgVerified() {
        return isOrgVerified;
    }

    @PropertyName(USERCONS.IS_ORG_VERIFIED)
    public void setOrgVerified(boolean orgVerified) {
        isOrgVerified = orgVerified;
    }
}

