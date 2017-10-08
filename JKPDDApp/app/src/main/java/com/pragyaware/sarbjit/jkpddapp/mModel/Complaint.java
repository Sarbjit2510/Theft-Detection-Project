package com.pragyaware.sarbjit.jkpddapp.mModel;


import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by C9 on 12/05/17.
 */
public class Complaint extends RealmObject implements Serializable {

    @Ignore
    public static final int SERVER_PENDING = 1;
    @Ignore
    public static final int SERVER_UPLOADED = 2;
    private RealmList<MultipleImage> images;
    @PrimaryKey
    private long ID;
    private long complaintID;
    private String state;
    private String district;
    private String address;
    private String division;
    private String defaulterName;
    private String lat;
    private String lng;
    private String complaintStatus;
    private String complaintDate;
    private String remarks;
    private String udpateStatusPhoto;
    private int locationFound;
    private int existingConsumer;
    private int theftDetected;
    private String accountNo;
    private String complaintContactID;
    private int penaltyAmount;

    // 1 pending, 2 uploaded
    private int serverStatus;
    private String markedTo;
    private String complaintResponse;

    public Complaint() {

    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getComplaintContactID() {
        return complaintContactID;
    }

    public void setComplaintContactID(String complaintContactID) {
        this.complaintContactID = complaintContactID;
    }

    public int getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(int penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getUdpateStatusPhoto() {
        return udpateStatusPhoto;
    }

    public void setUdpateStatusPhoto(String udpateStatusPhoto) {
        this.udpateStatusPhoto = udpateStatusPhoto;
    }

    public int getLocationFound() {
        return locationFound;
    }

    public void setLocationFound(int locationFound) {
        this.locationFound = locationFound;
    }

    public int getExistingConsumer() {
        return existingConsumer;
    }

    public void setExistingConsumer(int existingConsumer) {
        this.existingConsumer = existingConsumer;
    }

    public int getTheftDetected() {
        return theftDetected;
    }

    public void setTheftDetected(int theftDetected) {
        this.theftDetected = theftDetected;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMarkedTo() {
        return markedTo;
    }

    public void setMarkedTo(String markedTo) {
        this.markedTo = markedTo;
    }

    public String getComplaintResponse() {
        return complaintResponse;
    }

    public void setComplaintResponse(String complaintResponse) {
        this.complaintResponse = complaintResponse;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(long complaintID) {
        this.complaintID = complaintID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDefaulterName() {
        return defaulterName;
    }

    public void setDefaulterName(String defaulterName) {
        this.defaulterName = defaulterName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public RealmList<MultipleImage> getImages() {
        return images;
    }

    public void setImages(RealmList<MultipleImage> images) {
        this.images = images;
    }

}
