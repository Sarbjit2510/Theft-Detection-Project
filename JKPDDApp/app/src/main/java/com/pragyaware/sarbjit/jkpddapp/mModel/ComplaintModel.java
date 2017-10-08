package com.pragyaware.sarbjit.jkpddapp.mModel;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 05/13/2017.
 */
public class ComplaintModel extends RealmObject implements Serializable {

    private String ID, complaintComment, complaintAddress, complaintOfficer, complaintOfficerComment, complaintDefaulterExists, complaintDefaulterAcc,
            complaintDefaulterPenality, complaintStat, complaintStamp, complaintMedia, userId, complaintOfficerMedia;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getComplaintComment() {
        return complaintComment;
    }

    public void setComplaintComment(String complaintComment) {
        this.complaintComment = complaintComment;
    }

    public String getComplaintAddress() {
        return complaintAddress;
    }

    public void setComplaintAddress(String complaintAddress) {
        this.complaintAddress = complaintAddress;
    }

    public String getComplaintOfficer() {
        return complaintOfficer;
    }

    public void setComplaintOfficer(String complaintOfficer) {
        this.complaintOfficer = complaintOfficer;
    }

    public String getComplaintOfficerComment() {
        return complaintOfficerComment;
    }

    public void setComplaintOfficerComment(String complaintOfficerComment) {
        this.complaintOfficerComment = complaintOfficerComment;
    }

    public String getComplaintDefaulterExists() {
        return complaintDefaulterExists;
    }

    public void setComplaintDefaulterExists(String complaintDefaulterExists) {
        this.complaintDefaulterExists = complaintDefaulterExists;
    }

    public String getComplaintDefaulterAcc() {
        return complaintDefaulterAcc;
    }

    public void setComplaintDefaulterAcc(String complaintDefaulterAcc) {
        this.complaintDefaulterAcc = complaintDefaulterAcc;
    }

    public String getComplaintDefaulterPenality() {
        return complaintDefaulterPenality;
    }

    public void setComplaintDefaulterPenality(String complaintDefaulterPenality) {
        this.complaintDefaulterPenality = complaintDefaulterPenality;
    }

    public String getComplaintStat() {
        return complaintStat;
    }

    public void setComplaintStat(String complaintStat) {
        this.complaintStat = complaintStat;
    }

    public String getComplaintStamp() {
        return complaintStamp;
    }

    public void setComplaintStamp(String complaintStamp) {
        this.complaintStamp = complaintStamp;
    }

    public String getComplaintMedia() {
        return complaintMedia;
    }

    public void setComplaintMedia(String complaintMedia) {
        this.complaintMedia = complaintMedia;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComplaintOfficerMedia() {
        return complaintOfficerMedia;
    }

    public void setComplaintOfficerMedia(String complaintOfficerMedia) {
        this.complaintOfficerMedia = complaintOfficerMedia;
    }
}
